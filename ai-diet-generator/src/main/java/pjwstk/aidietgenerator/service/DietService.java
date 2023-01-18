package pjwstk.aidietgenerator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.entity.*;
import pjwstk.aidietgenerator.repository.*;
import pjwstk.aidietgenerator.request.DietRequest;
import pjwstk.aidietgenerator.service.Utils.ApiConstants;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public DietService(UserDetailsService userDetailsService, ProductRepository productRepository, ExcludedProductsListRepository excludedProductsListRepository,
                       DayDietRepository dayDietRepository, WeekDietRepository weekDietRepository, UserStatsRepository userStatsRepository, RecipeRepository recipeRepository,
                       IngredientRepository ingredientRepository){
        this.userDetailsService = userDetailsService;
        this.productRepository = productRepository;
        this.excludedProductsListRepository = excludedProductsListRepository;
        this.dayDietRepository = dayDietRepository;
        this.weekDietRepository = weekDietRepository;
        this.userStatsRepository = userStatsRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

//    Harris-Benedict Formula
    public double dailyBMR(double bodyWeight, int bodyHeight, int age, Gender gender, PhysicalActivity physicalActivity){

        double male = (88.362 + 13.397 * bodyWeight + 4.799 * bodyHeight - 5.677 * age) * physicalActivity.factor;
        double female = (447.593 + 9.247 * bodyWeight + 3.098 * bodyHeight - 4.330 * age) * physicalActivity.factor;

        return gender == MALE ? male : female;

    }

    public double goalCalories(double bodyWeight, int bodyHeight, int age, Gender gender, PhysicalActivity physicalActivity, DietGoal dietGoal){
        double bmr = dailyBMR(bodyWeight, bodyHeight, age, gender, physicalActivity);
        double kcalIntake = bmr;

        switch(dietGoal){
            case LOSE:
                kcalIntake = bmr - 500;
                if(kcalIntake <= 1200 && gender == FEMALE){
                    kcalIntake = 1200;
                }
                else if(kcalIntake <= 1500 && gender == MALE){
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
                kcalIntake = 44*bodyWeight;
                break;
        }

        return kcalIntake;
    }

    public List<Long> getRecommendedIds(Long UserId, Double threshold) throws IOException {
        URL url = new URL (ApiConstants.GENERATOR);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String jsonInputString = "{\"user_id\": " + UserId.toString() + ", " +
                "\"correlation_threshold\": " + threshold + "}";

        List<Long> recommendedIds = new ArrayList<>();


        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            String[] idList = response.substring(1, response.length() - 1).split(",");

            for(String id : idList){
                if(!id.isEmpty()) {
                    recommendedIds.add(Long.valueOf(id));
                }
            }

            return recommendedIds;
        }
    }

    public List<Long> getRecommendedReplacementIds(Long recipeId, Double threshold) throws IOException {
        URL url = new URL (ApiConstants.REPLACER);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String jsonInputString = "{\"dish_id\": " + recipeId + ", " +
                "\"correlation_threshold\": " + threshold + "}";

        List<Long> replacementsIds = new ArrayList<>();

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            String[] idList = response.substring(1, response.length() - 1).split(",");

            for(String id : idList){
                if(!id.isEmpty()) {
                    replacementsIds.add(Long.valueOf(id));
                }
            }

            return replacementsIds;
        }
    }

    public ExcludedProductsList setExcludedProducts(HttpServletResponse response, List<Long> productIds){
        User currentUser = userDetailsService.findCurrentUser();
        if (currentUser == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        } else {
            ExcludedProductsList excludedProductsList = excludedProductsListRepository.findByuser(currentUser);
            List<Product> listOfProducts = productRepository.findAllById(productIds);

            if(excludedProductsList == null) {
                excludedProductsList = new ExcludedProductsList(listOfProducts, currentUser);
            } else {
                excludedProductsList.setListOfExcludedProducts(listOfProducts);
            }

            excludedProductsListRepository.save(excludedProductsList);
            response.setStatus(HttpStatus.CREATED.value());

            return excludedProductsList;
        }
    }

    public Recipe getClosestRecipeToCaloriesNeed(List<Long> recipesIds, int mealsLeft, double remainingCalories, double accuracy){
        Recipe closestRecipe = new Recipe();
        double caloriesForAMeal = remainingCalories/mealsLeft;
        double closestCaloriesDif = Double.POSITIVE_INFINITY;

        for(Long recipeId : recipesIds){
            Recipe currentRecipe = recipeRepository.findById(recipeId).get();
            if(currentRecipe != null){
                int currentRecipeCalories = currentRecipe.getCalories();
                double caloriesDifference = Math.abs(caloriesForAMeal-currentRecipeCalories);

                if(caloriesDifference < closestCaloriesDif){
                    closestCaloriesDif = caloriesDifference;
                    closestRecipe = currentRecipe;
                }
            }
        }

        if(closestRecipe.getCalories()/caloriesForAMeal > 1+accuracy || closestRecipe.getCalories()/caloriesForAMeal < 1-accuracy){
            if(accuracy < 0.3) {
                return getClosestRecipeToCaloriesNeed(recipesIds, mealsLeft, remainingCalories, accuracy + 0.01);
            } else {
                return null;
            }
        }
        return closestRecipe;
    }
    public Recipe findRecipeWithLeastCalories(List<Long> recipeIds){
        Recipe leastCalRecipe = new Recipe();
        Double leastCal = Double.POSITIVE_INFINITY;

        for(Long id : recipeIds){
            Recipe currentRecipe = recipeRepository.findById(id).get();
            if(currentRecipe != null){
                if(currentRecipe.getCalories() < leastCal){
                    leastCalRecipe = currentRecipe;
                    leastCal = (double) currentRecipe.getCalories();
                }
            }
        }
        return leastCalRecipe;
    }

    public DietDay generateDietForDay(List<Long> recommendedRecipesIds, double caloriesPerDay, int mealsPerDay, List<Long> usedRecipesIds){
        DietDay dietDay = new DietDay();
        double remainingCalories = caloriesPerDay;
        List<Recipe> recipesToday = new ArrayList<>();

        for(int mealsLeft = mealsPerDay; mealsLeft>0;) {
            Recipe recommendedRecipe = getClosestRecipeToCaloriesNeed(recommendedRecipesIds, mealsLeft, remainingCalories, 0.01);

            if(recommendedRecipe != null){
                recommendedRecipesIds.remove(recommendedRecipe.getId());
                usedRecipesIds.add(recommendedRecipe.getId());
            } else {
                if(!usedRecipesIds.isEmpty()) {
                    recommendedRecipe = getClosestRecipeToCaloriesNeed(usedRecipesIds, mealsLeft, remainingCalories, 0.01);
                }
            }

            if(recommendedRecipe != null) {
                recipesToday.add(recommendedRecipe);
                remainingCalories -= recommendedRecipe.getCalories();
                mealsLeft--;

                if(remainingCalories < 0){
                    List<Long> allRecommendedRecipes = new ArrayList<>();
                    allRecommendedRecipes.addAll(recommendedRecipesIds);
                    allRecommendedRecipes.addAll(usedRecipesIds);

                    remainingCalories = findRecipeWithLeastCalories(allRecommendedRecipes).getCalories() * mealsLeft;
                }
            }
        }

        dietDay.setRecipesForToday(recipesToday);
        return dietDay;
    }
    public Boolean doesRecipeHaveExcludedProduct(Recipe recipe, List<Product> excludedProducts){
        List<Ingredient> currentRecipeIngredients = ingredientRepository.findByrecipe(recipe);

        for(Product excludedProduct : excludedProducts) {
            String currentProductName = excludedProduct.getName();
            for (Ingredient currentIngredient : currentRecipeIngredients) {
                if (currentProductName.equals(currentIngredient.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean doesRecipeMissRequirement(Recipe recipe, Boolean vegetarian, Boolean vegan, Boolean glutenFree, Boolean dairyFree, Boolean veryHealthy, Boolean verified){
        if(vegetarian != null){
            if(vegetarian){
                if(recipe.getVegetarian() != null) {
                    if (!recipe.getVegetarian()) return true;
                } else {
                    return true;
                }
            }
        }
        if(vegan != null){
            if(vegan){
                if(recipe.getVegan() != null) {
                    if (!recipe.getVegan()) return true;
                } else {
                    return true;
                }
            }
        }
        if(glutenFree != null){
            if(glutenFree){
                if(recipe.getGlutenFree() != null) {
                    if (!recipe.getGlutenFree()) return true;
                } else {
                    return true;
                }
            }
        }
        if(dairyFree != null){
            if(dairyFree){
                if(recipe.getDairyFree() != null) {
                    if (!recipe.getGlutenFree()) return true;
                } else {
                    return true;
                }
            }
        }
        if(veryHealthy != null){
            if(veryHealthy){
                if(recipe.getVeryHealthy() != null) {
                    if (!recipe.getVeryHealthy()) return true;
                } else {
                    return true;
                }
            }
        }
        if(verified != null){
            if(verified){
                if(recipe.getVerified() != null) {
                    if (!recipe.getVerified()) return true;
                } else {
                    return true;
                }
            }
        }

        return false;
    }

    public Long replaceRecipe(Long recipeToReplaceId, List<Product> excludedProductsList,
                              Boolean vegetarian, Boolean vegan, Boolean glutenFree, Boolean dairyFree, Boolean veryHealthy, Boolean verified,
                              double threshold) throws IOException {

        Boolean replaced = false;
        Long returnId = null;

        if(threshold < 0) {
            return returnId;
        }

        List<Long> replacementIds = getRecommendedReplacementIds(recipeToReplaceId, threshold);

        for(Long replacementId : replacementIds){
            Optional<Recipe> suggestedRecipe = recipeRepository.findById(replacementId);


            if(doesRecipeMissRequirement(suggestedRecipe.get(), vegetarian, vegan, glutenFree, dairyFree, veryHealthy, verified)) continue;
            if(doesRecipeHaveExcludedProduct(suggestedRecipe.get(), excludedProductsList)) continue;

            replaced = true;
            returnId = replacementId;

            if(replaced){
                break;
            }
        }

        return replaced == true ? returnId : replaceRecipe(recipeToReplaceId, excludedProductsList, vegetarian, vegan, glutenFree, dairyFree, veryHealthy, verified, threshold-0.1);
    }
    public List<Long> replaceRemovedRecipes(List<Long> removedRecipesIds, List<Product> excludedProductsList,
                                            Boolean vegetarian, Boolean vegan, Boolean glutenFree, Boolean dairyFree, Boolean veryHealthy, Boolean verified) throws IOException {
        List<Long> replacementRecipesIds = new ArrayList<>();
        double threshold = 0.7;

        for(Long removedRecipeId : removedRecipesIds){
            Long substituteRecipeId = replaceRecipe(removedRecipeId, excludedProductsList, vegetarian, vegan, glutenFree, dairyFree, veryHealthy, verified, threshold);

            if(substituteRecipeId != null){
                replacementRecipesIds.add(substituteRecipeId);
            }
        }

        return replacementRecipesIds;
    }
    public List<Long> getFilteredRecommendedIds(List<Long> recommendedIds, List<Product> excludedProductsList,
                                                Boolean vegetarian, Boolean vegan, Boolean glutenFree, Boolean dairyFree, Boolean veryHealthy, Boolean verified) throws IOException {
        List<Recipe> recommendedRecipes = recipeRepository.findAllById(recommendedIds);
        List<Long> newRecommendedIds = recommendedIds;
        List<Long> removedIds = new ArrayList<>();

        for(Recipe currentRecipe : recommendedRecipes){
            if(doesRecipeMissRequirement(currentRecipe, vegetarian, vegan, glutenFree, dairyFree, veryHealthy, verified)){
                removedIds.add(currentRecipe.getId());
                continue;
            }
            if(doesRecipeHaveExcludedProduct(currentRecipe, excludedProductsList) == true){
                removedIds.add(currentRecipe.getId());
            }
        }

        newRecommendedIds.removeAll(removedIds);

        List<Long> substitutesForRemovedIds = replaceRemovedRecipes(removedIds, excludedProductsList, vegetarian, vegan, glutenFree, dairyFree, veryHealthy, verified);
        newRecommendedIds.addAll(substitutesForRemovedIds);

        return newRecommendedIds;
    }
    public DietWeek generateDiet(DietRequest dietRequest, HttpServletResponse response) throws IOException {
        User currentUser = userDetailsService.findCurrentUser();
        DietWeek newDiet = new DietWeek();

        PhysicalActivity physicalActivity = dietRequest.getPhysicalActivity();
        DietGoal dietGoal = dietRequest.getDietGoal();
        List<Product> excludedProductsList = dietRequest.getExcludedProductsList();
        int mealsPerDay = dietRequest.getMealsPerDay();

        if(currentUser != null) {
            List<UserStats> currentUserStatsHistory = userStatsRepository.findByuser(currentUser);

            if(!currentUserStatsHistory.isEmpty()) {
                UserStats lastUserStats = currentUserStatsHistory.get(currentUserStatsHistory.size() - 1);

                double currentUserWeight = lastUserStats.getWeight();
                int currentUserHeight = lastUserStats.getHeight();
                int currentUserAge = lastUserStats.getAge();
                Gender currentUserGender = lastUserStats.getGender();

                if(currentUserWeight !=0 && currentUserHeight !=0 && currentUserAge !=0 && currentUserGender != null) {
                    double caloriesPerDay = goalCalories(currentUserWeight, currentUserHeight, currentUserAge, currentUserGender, physicalActivity, dietGoal);
                    lastUserStats.setCal((int) caloriesPerDay);

                    List<Long> recommendedRecipesIds = getRecommendedIds(currentUser.getId(), dietRequest.getThreshold());

                    recommendedRecipesIds = getFilteredRecommendedIds(recommendedRecipesIds, excludedProductsList,
                            dietRequest.getVegetarian(), dietRequest.getVegan(), dietRequest.getGlutenFree(),
                            dietRequest.getDairyFree(), dietRequest.getVeryHealthy(), dietRequest.getVerified());

                    if(recommendedRecipesIds.isEmpty()){
                        response.setStatus(HttpStatus.NO_CONTENT.value());
                        return null;
                    }

                    List<DietDay> dietWeek = new ArrayList<>();
                    List<Long> usedRecipesIds = new ArrayList<>();

                    for(int i=0; i<7; i++){
                        DietDay dietDay = generateDietForDay(recommendedRecipesIds, caloriesPerDay, mealsPerDay, usedRecipesIds);
                        dietDay.setDietWeek(newDiet);

                        usedRecipesIds.addAll(dietDay.getTodaysRecipesIds());
                        recommendedRecipesIds.removeAll(dietDay.getTodaysRecipesIds());
//                           dayDietRepository.save(dietDay);
                        dietWeek.add(dietDay);
                    }
                    newDiet.setDaysForWeekDiet(dietWeek);
                    newDiet.setUser(currentUser);

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

        response.setStatus(HttpStatus.CREATED.value());
        if(newDiet.getRecipeIdsForTheWeek().size() < mealsPerDay){
            dietRequest.setThreshold(dietRequest.getThreshold()-0.05);
            System.out.println("I'M GOING DEEPER WITH NEW THRESHOLD: " + dietRequest.getThreshold());
            return generateDiet(dietRequest, response);
        }
        return newDiet;
//        return weekDietRepository.save(newDiet);
    }
}
