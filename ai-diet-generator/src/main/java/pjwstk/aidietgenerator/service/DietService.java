package pjwstk.aidietgenerator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.entity.*;
import pjwstk.aidietgenerator.repository.*;
import pjwstk.aidietgenerator.request.DietRequest;
import pjwstk.aidietgenerator.request.RecipeReplaceRequest;
import pjwstk.aidietgenerator.service.Utils.ApiConstants;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static pjwstk.aidietgenerator.entity.Gender.FEMALE;
import static pjwstk.aidietgenerator.entity.Gender.MALE;

@Service
public class DietService {
    private final UserDetailsService userDetailsService;
    private final ProductRepository productRepository;
    private final ExcludedProductsListRepository excludedProductsListRepository;
    private final DayDietRepository dayDietRepository;
    private final WeekDietRepository weekDietRepository;
    private final UserStatsRepository userStatsRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public DietService(UserDetailsService userDetailsService, ProductRepository productRepository,
                       ExcludedProductsListRepository excludedProductsListRepository,
                       DayDietRepository dayDietRepository, WeekDietRepository weekDietRepository,
                       UserStatsRepository userStatsRepository, RecipeRepository recipeRepository,
                       IngredientRepository ingredientRepository) {
        this.userDetailsService = userDetailsService;
        this.productRepository = productRepository;
        this.excludedProductsListRepository = excludedProductsListRepository;
        this.dayDietRepository = dayDietRepository;
        this.weekDietRepository = weekDietRepository;
        this.userStatsRepository = userStatsRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public double dailyBMR(double bodyWeight, int bodyHeight, int age,
                           Gender gender, PhysicalActivity physicalActivity) {

        double male = (88.362 + 13.397 * bodyWeight + 4.799 * bodyHeight - 5.677 * age) * physicalActivity.factor;
        double female = (447.593 + 9.247 * bodyWeight + 3.098 * bodyHeight - 4.330 * age) * physicalActivity.factor;

        return gender == MALE ? male : female;

    }

    public double goalCalories(double bodyWeight, int bodyHeight, int age,
                               Gender gender, PhysicalActivity physicalActivity, DietGoal dietGoal) {
        double bmr = dailyBMR(bodyWeight, bodyHeight, age, gender, physicalActivity);
        double kcalIntake = bmr;

        switch (dietGoal) {
            case LOSE:
                kcalIntake = bmr - 500;
                if (kcalIntake <= 1200 && gender == FEMALE) {
                    kcalIntake = 1200;
                } else if (kcalIntake <= 1500 && gender == MALE) {
                    kcalIntake = 1500;
                }
                break;

            case MAINTAIN:
                kcalIntake = bmr;
                break;

            case GAIN:
                kcalIntake = bmr + 500;
                break;

            case MUSCLE:
                kcalIntake = 44 * bodyWeight;
                break;
        }

        return kcalIntake;
    }

    public List<Long> getRecommendedIds(Long UserId, Double threshold)
            throws IOException {
        URL url = new URL(ApiConstants.GENERATOR);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String jsonInputString = "{\"user_id\": " + UserId.toString() + ", " +
                "\"correlation_threshold\": " + threshold + "}";

        List<Long> recommendedIds = new ArrayList<>();


        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            String[] idList = response.substring(1, response.length() - 1).split(",");

            for (String id : idList) {
                if (!id.isEmpty()) {
                    recommendedIds.add(Long.valueOf(id));
                }
            }

            return recommendedIds;
        }
    }

    public List<Long> getRecommendedReplacementIds(Long recipeId, Double threshold)
            throws IOException {
        URL url = new URL(ApiConstants.REPLACER);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String jsonInputString = "{\"dish_id\": " + recipeId + ", " +
                "\"correlation_threshold\": " + threshold + "}";

        List<Long> replacementsIds = new ArrayList<>();

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            String[] idList = response.substring(1, response.length() - 1).split(",");

            for (String id : idList) {
                if (!id.isEmpty()) {
                    replacementsIds.add(Long.valueOf(id));
                }
            }

            return replacementsIds;
        }
    }

    public ExcludedProductsList setExcludedProducts(HttpServletResponse response,
                                                    List<Long> productIds) {
        User currentUser = userDetailsService.findCurrentUser();
        if (currentUser == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        } else {
            ExcludedProductsList excludedProductsList = excludedProductsListRepository.findByuser(currentUser);
            List<Product> listOfProducts = productRepository.findAllById(productIds);

            if (excludedProductsList == null) {
                excludedProductsList = new ExcludedProductsList(listOfProducts, currentUser);
            } else {
                excludedProductsList.setListOfExcludedProducts(listOfProducts);
            }

            excludedProductsListRepository.save(excludedProductsList);
            response.setStatus(HttpStatus.CREATED.value());

            return excludedProductsList;
        }
    }

    public Recipe getClosestRecipeToCaloriesNeed(List<Long> recipesIds, int mealsLeft,
                                                 double remainingCalories, double accuracy) {
        Recipe closestRecipe = new Recipe();
        double caloriesForAMeal = remainingCalories / mealsLeft;
        double closestCaloriesDif = Double.POSITIVE_INFINITY;

        for (Long recipeId : recipesIds) {
            Recipe currentRecipe = recipeRepository.findById(recipeId).get();
            if (currentRecipe != null) {
                int currentRecipeCalories = currentRecipe.getCalories();
                double caloriesDifference = Math.abs(caloriesForAMeal - currentRecipeCalories);

                if (caloriesDifference < closestCaloriesDif) {
                    closestCaloriesDif = caloriesDifference;
                    closestRecipe = currentRecipe;
                }
            }
        }

        if (closestRecipe.getCalories() != null) {
            if (closestRecipe.getCalories() / caloriesForAMeal > 1 + accuracy || closestRecipe.getCalories() / caloriesForAMeal < 1 - accuracy) {
                if (accuracy < 0.3) {
                    return getClosestRecipeToCaloriesNeed(recipesIds, mealsLeft, remainingCalories, accuracy + 0.01);
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
        return closestRecipe;
    }

    public Recipe findRecipeWithLeastCalories(List<Long> recipeIds) {
        Recipe leastCalRecipe = new Recipe();
        Double leastCal = Double.POSITIVE_INFINITY;

        for (Long id : recipeIds) {
            Recipe currentRecipe = recipeRepository.findById(id).get();
            if (currentRecipe != null) {
                if (currentRecipe.getCalories() < leastCal) {
                    leastCalRecipe = currentRecipe;
                    leastCal = (double) currentRecipe.getCalories();
                }
            }
        }
        return leastCalRecipe;
    }

    public Recipe getExpandedSearchClosestRecipeToCaloriesNeed(List<Long> recipesListIds, int mealsLeft,
                                                               double remainingCalories, double accuracy) {
        Recipe recommendedRecipe = getClosestRecipeToCaloriesNeed(recipesListIds, mealsLeft,
                remainingCalories * (1 + 0.1 * mealsLeft), accuracy);
        if (recommendedRecipe == null) {
            recommendedRecipe = getClosestRecipeToCaloriesNeed(recipesListIds, mealsLeft,
                    remainingCalories * (1 - 0.1 * mealsLeft), accuracy);
        }

        return recommendedRecipe;
    }

    public Boolean doRecipesFillMacroRequirements(List<Recipe> recipes, DietGoal dietGoal) {
        double todayProtein = 0;
        double todayFat = 0;
        double todayCarbs = 0;

        for (Recipe recipe : recipes) {
            todayProtein += recipe.getProtein();
            todayFat += recipe.getFat();
            todayCarbs += recipe.getCarbs();
        }

        double todayMacro = todayProtein + todayFat + todayCarbs;
        double carbsRatio = todayCarbs / todayMacro * 100;
        double fatRatio = todayFat / todayMacro * 100;
        double proteinRatio = todayProtein / todayMacro * 100;

        switch (dietGoal) {
            case LOSE:
                if (carbsRatio < 10 || carbsRatio > 30) {
                    return false;
                }
                if (fatRatio < 30 || fatRatio > 40) {
                    return false;
                }
                if (proteinRatio < 40 || proteinRatio > 50) {
                    return false;
                }
                break;
            case MAINTAIN:
                if (carbsRatio < 45 || carbsRatio > 65) {
                    return false;
                }
                if (fatRatio < 20 || fatRatio > 35) {
                    return false;
                }
                if (proteinRatio < 10 || proteinRatio > 35) {
                    return false;
                }
                break;
            case GAIN:
                if (carbsRatio < 50 || carbsRatio > 60) {
                    return false;
                }
                if (fatRatio < 20 || fatRatio > 30) {
                    return false;
                }
                if (proteinRatio < 15 || proteinRatio > 25) {
                    return false;
                }
                break;
            case MUSCLE:
                if (carbsRatio < 30 || carbsRatio > 40) {
                    return false;
                }
                if (fatRatio < 30 || fatRatio > 40) {
                    return false;
                }
                if (proteinRatio < 30 || proteinRatio > 40) {
                    return false;
                }
                break;
        }
        return true;
    }

    public DietDay generateDietForDay(List<Long> startingRecommendedRecipesIds,
                                      double caloriesPerDay, int mealsPerDay,
                                      List<Long> startingUsedRecipesIds, DietGoal dietGoal,
                                      Boolean macroCheck) {

        DietDay dietDay = new DietDay();
        double remainingCalories = caloriesPerDay;
        List<Recipe> recipesToday = new ArrayList<>();

        List<Long> recommendedRecipesIds = new ArrayList<>(startingRecommendedRecipesIds);
        Collections.shuffle(recommendedRecipesIds);
        List<Long> usedRecipesIds = new ArrayList<>(startingUsedRecipesIds);

        List<Long> allRecipeIds = new ArrayList<>();
        allRecipeIds.addAll(recommendedRecipesIds);
        allRecipeIds.addAll(usedRecipesIds);
        boolean firstRecipe = true;
        int firstRecipeIndex = 0;

        for (int mealsLeft = mealsPerDay; mealsLeft > 0;) {
            Recipe recommendedRecipe;
            if (firstRecipe) {
                dietDay = new DietDay();
                recommendedRecipe = recipeRepository.findById(allRecipeIds.get(firstRecipeIndex)).get();
                remainingCalories = caloriesPerDay;
                mealsLeft = mealsPerDay;
                firstRecipe = false;
                recipesToday.clear();

            } else {
                recommendedRecipe = getClosestRecipeToCaloriesNeed(recommendedRecipesIds, mealsLeft, remainingCalories, 0.01);
                if (recommendedRecipe == null) {
                    if (!recommendedRecipesIds.isEmpty()) {
                        if (mealsLeft > 1) {
                            recommendedRecipe = getExpandedSearchClosestRecipeToCaloriesNeed(recommendedRecipesIds, mealsLeft, remainingCalories, 0.01);
                        }
                    }
                }

                if (recommendedRecipe == null) {
                    if (!usedRecipesIds.isEmpty()) {
                        recommendedRecipe = getClosestRecipeToCaloriesNeed(usedRecipesIds, mealsLeft, remainingCalories, 0.01);
                        if (recommendedRecipe == null) {
                            if (mealsLeft > 1) {
                                recommendedRecipe = getExpandedSearchClosestRecipeToCaloriesNeed(usedRecipesIds, mealsLeft, remainingCalories, 0.01);
                            }
                        }
                    }
                }
            }

            if (recommendedRecipe != null) {
                if (!recipesToday.contains(recommendedRecipe)) {
                    recipesToday.add(recommendedRecipe);

                    remainingCalories -= recommendedRecipe.getCalories();
                    mealsLeft--;

                    if (remainingCalories <= 0) {
                        List<Long> allRecommendedRecipes = new ArrayList<>();
                        allRecommendedRecipes.addAll(recommendedRecipesIds);
                        allRecommendedRecipes.addAll(usedRecipesIds);

                        remainingCalories = findRecipeWithLeastCalories(allRecommendedRecipes).getCalories();
                    }

                    if (mealsLeft == 0) {
                        if (macroCheck) {
                            if (!doRecipesFillMacroRequirements(recipesToday, dietGoal)) {
                                firstRecipeIndex++;
                                firstRecipe = true;
                                mealsLeft = mealsPerDay;
                                if (firstRecipeIndex >= allRecipeIds.size() - 1) {
                                    return null;
                                }
                            }
                        }
                    }
                } else {
                    usedRecipesIds.remove(recommendedRecipe.getId());
                    recommendedRecipesIds.remove(recommendedRecipe.getId());
                }
            } else {
                firstRecipeIndex++;
                firstRecipe = true;
                if (firstRecipeIndex >= allRecipeIds.size() - 1) {
                    return null;
                }
            }
        }
        dietDay.setRecipesForToday(recipesToday);
        return dietDay;
    }

    public Boolean doesRecipeHaveExcludedProduct(Recipe recipe, List<Product> excludedProducts) {
        List<Ingredient> currentRecipeIngredients = ingredientRepository.findByrecipe(recipe);

        for (Product excludedProduct : excludedProducts) {
            String currentProductName = excludedProduct.getName();
            for (Ingredient currentIngredient : currentRecipeIngredients) {
                if (currentProductName.equals(currentIngredient.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean doesRecipeMissRequirement(Recipe recipe, Boolean vegetarian, Boolean vegan,
                                             Boolean glutenFree, Boolean dairyFree,
                                             Boolean veryHealthy) {
        if (recipe.getVerified() != null) {
            if (!recipe.getVerified()) return true;
        } else {
            return true;
        }

        if (vegetarian != null) {
            if (vegetarian) {
                if (recipe.getVegetarian() != null) {
                    if (!recipe.getVegetarian()) return true;
                } else {
                    return true;
                }
            }
        }
        if (vegan != null) {
            if (vegan) {
                if (recipe.getVegan() != null) {
                    if (!recipe.getVegan()) return true;
                } else {
                    return true;
                }
            }
        }
        if (glutenFree != null) {
            if (glutenFree) {
                if (recipe.getGlutenFree() != null) {
                    if (!recipe.getGlutenFree()) return true;
                } else {
                    return true;
                }
            }
        }
        if (dairyFree != null) {
            if (dairyFree) {
                if (recipe.getDairyFree() != null) {
                    if (!recipe.getDairyFree()) return true;
                } else {
                    return true;
                }
            }
        }
        if (veryHealthy != null) {
            if (veryHealthy) {
                if (recipe.getVeryHealthy() != null) {
                    if (!recipe.getVeryHealthy()) return true;
                } else {
                    return true;
                }
            }
        }

        if (recipe.getCalories() == null) return true;

        return false;
    }

    public Long replaceRecipe(Long recipeToReplaceId, List<Product> excludedProductsList,
                              Boolean vegetarian, Boolean vegan, Boolean glutenFree, Boolean dairyFree,
                              Boolean veryHealthy, Boolean verified,
                              double threshold) throws IOException {

        boolean replaced = false;
        Long returnId = null;

        if (threshold < 0.6) {
            return returnId;
        }

        List<Long> replacementIds = getRecommendedReplacementIds(recipeToReplaceId, threshold);
        List<Recipe> replacementRecipes = recipeRepository.findAllById(replacementIds);

        for (Recipe suggestedRecipe : replacementRecipes) {
            if (doesRecipeMissRequirement(suggestedRecipe, vegetarian, vegan, glutenFree, dairyFree, veryHealthy))
                continue;
            if (doesRecipeHaveExcludedProduct(suggestedRecipe, excludedProductsList)) continue;

            replaced = true;
            returnId = suggestedRecipe.getId();

            if (replaced) {
                break;
            }
        }

        return replaced ? returnId : replaceRecipe(recipeToReplaceId, excludedProductsList, vegetarian, vegan,
                glutenFree, dairyFree, veryHealthy, verified, threshold - 0.1);
    }

    public Recipe replaceRecipeFromADay(RecipeReplaceRequest recipeReplaceRequest, HttpServletResponse response) {
        User currentUser = userDetailsService.findCurrentUser();
        if (currentUser != null) {
            DietWeek dietToChange = weekDietRepository.findByuser(currentUser);

            Long dayToChangeId = recipeReplaceRequest.getDayDietId();
            Long recipeToReplaceId = recipeReplaceRequest.getRecipeToReplaceId();
            Boolean vegetarian = false;
            Boolean vegan = false;
            Boolean glutenFree = false;
            Boolean dairyFree = false;
            Boolean veryHealthy = false;
            List<Product> excludedProductsList = null;

            if(recipeReplaceRequest.getVegetarian() != null){
                vegetarian = recipeReplaceRequest.getVegetarian();
            } else {
                if(dietToChange.getVegetarian() != null){
                    vegetarian = dietToChange.getVegetarian();
                }
            }
            if(recipeReplaceRequest.getVegan() != null){
                vegan = recipeReplaceRequest.getVegan();
            } else {
                if(dietToChange.getVegan() != null){
                    vegan = dietToChange.getVegan();
                }
            }
            if(recipeReplaceRequest.getGlutenFree() != null){
                glutenFree = recipeReplaceRequest.getGlutenFree();
            } else {
                if(dietToChange.getGlutenFree() != null){
                    glutenFree = dietToChange.getGlutenFree();
                }
            }
            if(recipeReplaceRequest.getDairyFree() != null){
                dairyFree = recipeReplaceRequest.getDairyFree();
            } else {
                if(dietToChange.getDairyFree() != null){
                    dairyFree = dietToChange.getDairyFree();
                }
            }
            if(recipeReplaceRequest.getVeryHealthy() != null){
                veryHealthy = recipeReplaceRequest.getVeryHealthy();
            } else {
                if(dietToChange.getVeryHealthy() != null){
                    veryHealthy = dietToChange.getVeryHealthy();
                }
            }

            if(recipeReplaceRequest.getExcludedProductsList() != null){
                excludedProductsList = recipeReplaceRequest.getExcludedProductsList();
            } else {
                if(excludedProductsListRepository.findByuser(currentUser) != null){
                    excludedProductsList = excludedProductsListRepository.findByuser(currentUser).getListOfExcludedProducts();
                }
            }

            DietDay dayToChange = dayDietRepository.findById(dayToChangeId).get();

            if (dietToChange != null) {
                Recipe recipeToReplace = recipeRepository.findById(recipeToReplaceId).get();
                if (dayToChange != null) {
                    boolean changed = false;
                    if (recipeToReplace != null) {
                        List<Recipe> allRecipes = recipeRepository.findAll();
                        allRecipes.remove(recipeToReplace);
                        Collections.shuffle(allRecipes);
                        int currentRecipeIndex = 0;
                        Recipe suggestedRecipe = null;

                        while (!changed) {
                            suggestedRecipe = allRecipes.get(currentRecipeIndex);
                            if (doesRecipeHaveExcludedProduct(suggestedRecipe, excludedProductsList)) {
                                currentRecipeIndex++;
                                if (currentRecipeIndex >= allRecipes.size()) {
                                    response.setStatus(HttpStatus.NO_CONTENT.value());
                                    return null;
                                }
                                continue;
                            }
                            if (doesRecipeMissRequirement(suggestedRecipe, vegetarian, vegan, glutenFree,
                                    dairyFree, veryHealthy)) {
                                currentRecipeIndex++;
                                if (currentRecipeIndex >= allRecipes.size()) {
                                    response.setStatus(HttpStatus.NO_CONTENT.value());
                                    return null;
                                }
                                continue;
                            }

                            if (suggestedRecipe != null && !dayToChange.getRecipesForToday().contains(suggestedRecipe)) {
                                Integer oldRecipeCalories = recipeToReplace.getCalories();
                                Integer suggestedRecipeCalories = suggestedRecipe.getCalories();
                                if (oldRecipeCalories != null && suggestedRecipeCalories != null) {
                                    int caloriesDifference = Math.abs(oldRecipeCalories - suggestedRecipeCalories);
                                    if (caloriesDifference < 100) {
                                        List<Recipe> daysRecipes = dayToChange.getRecipesForToday();
                                        daysRecipes.remove(recipeToReplace);
                                        daysRecipes.add(suggestedRecipe);
                                        dayToChange.setRecipesForToday(daysRecipes);
                                        dayDietRepository.save(dayToChange);
                                        weekDietRepository.save(dietToChange);
                                        changed = true;
                                    } else {
                                        currentRecipeIndex++;
                                        if (currentRecipeIndex >= allRecipes.size()) {
                                            response.setStatus(HttpStatus.NO_CONTENT.value());
                                            return null;
                                        }
                                    }
                                } else {
                                    currentRecipeIndex++;
                                    if (currentRecipeIndex >= allRecipes.size()) {
                                        response.setStatus(HttpStatus.NO_CONTENT.value());
                                        return null;
                                    }
                                }
                            } else {
                                currentRecipeIndex++;
                                if (currentRecipeIndex >= allRecipes.size()) {
                                    response.setStatus(HttpStatus.NO_CONTENT.value());
                                    return null;
                                }
                            }
                        }
                        response.setStatus(HttpStatus.OK.value());
                        return suggestedRecipe;
                    } else {
                        response.setStatus(HttpStatus.NO_CONTENT.value());
                        return null;
                    }
                } else {
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                    return null;
                }
            } else {
                response.setStatus(HttpStatus.NO_CONTENT.value());
                return null;
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        }
    }

    public List<Long> replaceRemovedRecipes(List<Long> removedRecipesIds, List<Product> excludedProductsList,
                                            Boolean vegetarian, Boolean vegan, Boolean glutenFree, Boolean dairyFree,
                                            Boolean veryHealthy, Boolean verified) throws IOException {
        List<Long> replacementRecipesIds = new ArrayList<>();
        double threshold = 0.7;

        for (Long removedRecipeId : removedRecipesIds) {
            Long substituteRecipeId = replaceRecipe(removedRecipeId, excludedProductsList, vegetarian, vegan, glutenFree,
                    dairyFree, veryHealthy, verified, threshold);

            if (substituteRecipeId != null && !replacementRecipesIds.contains(substituteRecipeId)) {
                replacementRecipesIds.add(substituteRecipeId);
            }
        }

        return replacementRecipesIds;
    }

    public List<Long> getFilteredRecommendedIds(List<Long> recommendedIds, List<Product> excludedProductsList,
                                                Boolean vegetarian, Boolean vegan, Boolean glutenFree,
                                                Boolean dairyFree, Boolean veryHealthy, Boolean verified, Boolean personalized) throws IOException {
        List<Recipe> recommendedRecipes = recipeRepository.findAllById(recommendedIds);
        List<Long> removedIds = new ArrayList<>();

        for (Recipe currentRecipe : recommendedRecipes) {
            if (doesRecipeMissRequirement(currentRecipe, vegetarian, vegan, glutenFree, dairyFree, veryHealthy)) {
                removedIds.add(currentRecipe.getId());
                continue;
            }
            if (doesRecipeHaveExcludedProduct(currentRecipe, excludedProductsList)) {
                removedIds.add(currentRecipe.getId());
            }
        }
        recommendedIds.removeAll(removedIds);

        if (personalized) {
            List<Long> substitutesForRemovedIds = replaceRemovedRecipes(removedIds, excludedProductsList, vegetarian, vegan, glutenFree, dairyFree, veryHealthy, verified);
            recommendedIds.addAll(substitutesForRemovedIds);
        }

        return recommendedIds;
    }

    public DietWeek generateDiet(DietRequest dietRequest, HttpServletResponse response)
            throws IOException {
        User currentUser = userDetailsService.findCurrentUser();

        PhysicalActivity physicalActivity = dietRequest.getPhysicalActivity();
        DietGoal dietGoal = dietRequest.getDietGoal();

        List<Product> excludedProductsList = new ArrayList<>();
        if (dietRequest.getExcludedProductsList() != null) {
            excludedProductsList = dietRequest.getExcludedProductsList();
        }

        int mealsPerDay = dietRequest.getMealsPerDay();

        if (currentUser != null) {
            List<UserStats> currentUserStatsHistory = userStatsRepository.findByuser(currentUser);
            DietWeek currentUserDiet = weekDietRepository.findByuser(currentUser);

            if (currentUserDiet == null) {
                currentUserDiet = new DietWeek();
                currentUserDiet.setUser(currentUser);
            }

            if (!currentUserStatsHistory.isEmpty()) {
                UserStats lastUserStats = currentUserStatsHistory.get(currentUserStatsHistory.size() - 1);

                double currentUserWeight = lastUserStats.getWeight();
                int currentUserHeight = lastUserStats.getHeight();
                int currentUserAge = lastUserStats.getAge();
                Gender currentUserGender = lastUserStats.getGender();

                if (currentUserWeight != 0 && currentUserHeight != 0 && currentUserAge != 0 && currentUserGender != null) {
                    if (mealsPerDay >= 3 && mealsPerDay <= 5) {
                        double caloriesPerDay = goalCalories(currentUserWeight, currentUserHeight,
                                currentUserAge, currentUserGender, physicalActivity, dietGoal);
                        lastUserStats.setCal((int) caloriesPerDay);
                        userStatsRepository.save(lastUserStats);

                        List<Long> recommendedRecipesIds = new ArrayList<>();
                        if (dietRequest.getPersonalized() == null) {
                            dietRequest.setPersonalized(false);
                        }
                        if (dietRequest.getMacroCheck() == null) {
                            dietRequest.setMacroCheck(true);
                        }

                        if (dietRequest.getPersonalized()) {
                            recommendedRecipesIds = getRecommendedIds(currentUser.getId(), dietRequest.getThreshold());
                        } else {
                            List<Recipe> allRecipes = recipeRepository.findAll();
                            for (Recipe recipe : allRecipes) {
                                recommendedRecipesIds.add(recipe.getId());
                            }
                            dietRequest.setThreshold(null);
                        }
                        recommendedRecipesIds = getFilteredRecommendedIds(recommendedRecipesIds, excludedProductsList,
                                dietRequest.getVegetarian(), dietRequest.getVegan(), dietRequest.getGlutenFree(),
                                dietRequest.getDairyFree(), dietRequest.getVeryHealthy(), dietRequest.getVerified(), dietRequest.getPersonalized());

                        if (recommendedRecipesIds.isEmpty()) {
                            response.setStatus(HttpStatus.NO_CONTENT.value());
                            return null;
                        }

                        List<DietDay> dietWeek = new ArrayList<>();
                        List<Long> usedRecipesIds = new ArrayList<>();

                        for (int i = 0; i < 7; i++) {
                            DietDay dietDay = generateDietForDay(recommendedRecipesIds, caloriesPerDay,
                                    mealsPerDay, usedRecipesIds, dietGoal, dietRequest.getMacroCheck());

                            if (dietDay == null) {
                                if (dietRequest.getThreshold() != null) {
                                    dietRequest.setThreshold(dietRequest.getThreshold() - 0.05);
                                    if (dietRequest.getThreshold() < 0.75) {
                                        response.setStatus(HttpStatus.NO_CONTENT.value());
                                        return null;
                                    }
                                }

                                if (!dietWeek.isEmpty()) {
                                    if (dietRequest.getThreshold() == null || dietRequest.getThreshold() <= 0.8) {
                                        for (; i < 7; i++) {
                                            DietDay alreadyUsedDietDay = dietWeek
                                                    .get((int) (Math.random() * (dietWeek.size() - 1)));

                                            List<DietWeek> dietWeeksOfDietDay = new ArrayList<>();
                                            if (alreadyUsedDietDay.getDietWeek() != null) {
                                                dietWeeksOfDietDay = alreadyUsedDietDay.getDietWeek();
                                            }
                                            dietWeeksOfDietDay.add(currentUserDiet);
                                            alreadyUsedDietDay.setDietWeek(dietWeeksOfDietDay);

                                            dayDietRepository.save(alreadyUsedDietDay);
                                            dietWeek.add(alreadyUsedDietDay);
                                        }
                                    }

                                    currentUserDiet.setDaysForWeekDiet(dietWeek);
                                    currentUserDiet.setCreatedAt();
                                    response.setStatus(HttpStatus.CREATED.value());
                                    return weekDietRepository.save(currentUserDiet);
                                } else {
                                    if (dietRequest.getThreshold() != null) {
                                        return generateDiet(dietRequest, response);
                                    } else {
                                        response.setStatus(HttpStatus.NO_CONTENT.value());
                                        return null;
                                    }
                                }
                            }
                            for (Long todaysRecipeId : dietDay.getTodaysRecipesIds()) {
                                recommendedRecipesIds.remove(todaysRecipeId);
                                if (!usedRecipesIds.contains(todaysRecipeId)) {
                                    usedRecipesIds.add(todaysRecipeId);
                                }
                            }

                            List<DietWeek> dietWeeksOfCurrentDay = new ArrayList<>();
                            if (dietDay.getDietWeek() != null) {
                                dietWeeksOfCurrentDay = dietDay.getDietWeek();
                            }

                            dietWeeksOfCurrentDay.add(currentUserDiet);
                            dietDay.setDietWeek(dietWeeksOfCurrentDay);

                            dayDietRepository.save(dietDay);
                            dietWeek.add(dietDay);
                        }
                        currentUserDiet.setDaysForWeekDiet(dietWeek);
                        currentUserDiet.setCreatedAt();

                        currentUserDiet.setDietGoal(dietGoal);
                        currentUserDiet.setStartingWeight(lastUserStats.getWeight());

                        currentUserDiet.setVegetarian(dietRequest.getVegetarian());
                        currentUserDiet.setVegan(dietRequest.getVegan());
                        currentUserDiet.setGlutenFree(dietRequest.getGlutenFree());
                        currentUserDiet.setDairyFree(dietRequest.getDairyFree());
                        currentUserDiet.setVeryHealthy(dietRequest.getVeryHealthy());
                        currentUserDiet.setVerified(dietRequest.getVerified());

                        response.setStatus(HttpStatus.CREATED.value());

                        return weekDietRepository.save(currentUserDiet);
                    } else {
                        response.setStatus(HttpStatus.BAD_REQUEST.value());
                        return null;
                    }

                } else {
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                    return null;
                }
            } else {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return null;
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        }
    }
}
