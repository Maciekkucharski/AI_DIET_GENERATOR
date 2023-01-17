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

    public List<Long> getRecommendedIds(Long UserId) throws IOException {
        URL url = new URL (ApiConstants.GENERATOR);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String jsonInputString = "{\"user_id\": " + UserId.toString() + ", " +
                "\"correlation_threshold\": 0.8}";

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
                recommendedIds.add(Long.valueOf(id));
            }

            return recommendedIds;
        }
    }

    public List<Long> getRecommendedReplacementIds(Long RecipeId) throws IOException {
        URL url = new URL (ApiConstants.REPLACER);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String jsonInputString = "{\"dish_id\": " + RecipeId + ", " +
                "\"correlation_threshold\": 0.7}";

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
                replacementsIds.add(Long.valueOf(id));
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
    public DietDay generateDietForDay(List<Long> recommendedRecipesIds, double caloriesPerDay, int mealsPerDay, List<Long> usedRecipesIds){
        DietDay dietDay = new DietDay();
        double remainingCalories = caloriesPerDay;
        List<Recipe> recipesToday = new ArrayList<>();
        int addedMealsForToday = 0;


        for (Long id : recommendedRecipesIds) {
            Optional<Recipe> currentRecipe = recipeRepository.findById(id);
            if (currentRecipe.get().getCalories() < remainingCalories) {
                recipesToday.add(currentRecipe.get());
                remainingCalories = remainingCalories - currentRecipe.get().getCalories();
                addedMealsForToday++;

                if(addedMealsForToday == mealsPerDay){
                    break;
                }
            }
        }

        if(addedMealsForToday < mealsPerDay) {
            for (Long id : usedRecipesIds) {
                Optional<Recipe> currentUsedRecipe = recipeRepository.findById(id);
                if (currentUsedRecipe.get().getCalories() < remainingCalories) {
                    recipesToday.add(currentUsedRecipe.get());
                    remainingCalories = remainingCalories - currentUsedRecipe.get().getCalories();
                    addedMealsForToday++;

                    if (addedMealsForToday == mealsPerDay) {
                        break;
                    }
                }
            }
        }
        dietDay.setRecipesForToday(recipesToday);
        return dietDay;
    }

//    public Long replaceRecipe(Long recipeId){
//
//    }

    public Boolean checkForExcludedProducts(Recipe recipe, List<Product> excludedProducts){
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

    
//  TERA TUTAJ JEDEN PRZEPIS ZAMIENIA NA JEDEN PRZEPIS JEZELI DA RADE
    public List<Long> replaceRemovedRecipes(List<Long> removedRecipesIds, List<Product> excludedProductsList) throws IOException {
        int numberOfRemovedRecipes = removedRecipesIds.size();
        List<Long> replacementRecipesIds = new ArrayList<>();

        for(Long removedRecipeId : removedRecipesIds){
            List<Long> replacementIds = getRecommendedReplacementIds(removedRecipeId);
            boolean replaced = false;

            for(Long replacementId : replacementIds){
                Optional<Recipe> suggestedRecipe = recipeRepository.findById(replacementId);

                if(checkForExcludedProducts(suggestedRecipe.get(), excludedProductsList) == true){
                    break;
                }

                if(replaced){
                    break;
                }
            }
        }

        return replacementRecipesIds;
    }
    public List<Long> getFilteredRecommendedIds(List<Long> recommendedIds, List<Product> excludedProductsList){
        List<Recipe> recommendedRecipes = recipeRepository.findAllById(recommendedIds);
        List<Long> newRecommendedIds = recommendedIds;
        List<Long> removedIds = new ArrayList<>();

        for(Recipe currentRecipe : recommendedRecipes){
            if(checkForExcludedProducts(currentRecipe, excludedProductsList) == true){
                newRecommendedIds.remove(currentRecipe.getId());
                removedIds.add(currentRecipe.getId());
                break;
            }
        }

//  TUTAJ WDUPIE TEN ZASTEPUJACY ALGORYTM HEHE
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

                    List<Long> recommendedRecipesIds = getRecommendedIds(currentUser.getId());
                    recommendedRecipesIds = getFilteredRecommendedIds(recommendedRecipesIds, excludedProductsList);

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
                        recommendedRecipesIds.removeAll(usedRecipesIds);
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
        return newDiet;
//        return weekDietRepository.save(newDiet);
    }
}
