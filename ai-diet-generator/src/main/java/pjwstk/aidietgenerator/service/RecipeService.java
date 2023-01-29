package pjwstk.aidietgenerator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.entity.*;
import pjwstk.aidietgenerator.exception.ResourceNotFoundException;
import pjwstk.aidietgenerator.repository.*;
import pjwstk.aidietgenerator.request.RecipeReplaceRequest;
import pjwstk.aidietgenerator.request.RecipeRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final RecipeLikesRepository recipeLikesRepository;
    private final RecipeCommentsRepository recipeCommentsRepository;
    private final DayDietRepository dayDietRepository;
    private final DietService dietService;
    private final WeekDietRepository weekDietRepository;
    private final ExcludedProductsListRepository excludedProductsListRepository;
    private final RatingRepository ratingRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository,
                         IngredientRepository ingredientRepository,
                         UserDetailsService userDetailsService,
                         UserRepository userRepository,
                         DayDietRepository dayDietRepository,
                         RecipeLikesRepository recipeLikesRepository,
                         RecipeCommentsRepository recipeCommentsRepository,
                         DietService dietService,
                         WeekDietRepository weekDietRepository,
                         ExcludedProductsListRepository excludedProductsListRepository,
                         RatingRepository ratingRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.dayDietRepository = dayDietRepository;
        this.recipeLikesRepository = recipeLikesRepository;
        this.recipeCommentsRepository = recipeCommentsRepository;
        this.dietService = dietService;
        this.weekDietRepository = weekDietRepository;
        this.excludedProductsListRepository = excludedProductsListRepository;
        this.ratingRepository = ratingRepository;
    }


    public Recipe addRecipe(RecipeRequest recipeRequest, HttpServletResponse response) {
        User currentUser = userDetailsService.findCurrentUser();
        if (currentUser == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        } else {
            if (recipeRequest.getTitle() == null || recipeRequest.getInstructions() == null || recipeRequest.getRecipesIngredients() == null) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return null;
            } else {
                Recipe newRecipe = new Recipe();
                if(recipeRequest.getTitle() != null) {
                    newRecipe.setTitle(recipeRequest.getTitle());
                }
                if(recipeRequest.getServings() != null) {
                    newRecipe.setServings(recipeRequest.getServings());
                }
                if(recipeRequest.getReadyInMinutes() != null) {
                    newRecipe.setReadyInMinutes(recipeRequest.getReadyInMinutes());
                }
                if(recipeRequest.getImagePath() != null) {
                    newRecipe.setImagePath(recipeRequest.getImagePath());
                }
                if(recipeRequest.getInstructions() != null) {
                    newRecipe.setInstructions(recipeRequest.getInstructions());
                }
                if(recipeRequest.isVegetarian() != null) {
                    newRecipe.setVegetarian(recipeRequest.isVegetarian());
                }
                if(recipeRequest.isVegan() != null) {
                    newRecipe.setVegan(recipeRequest.isVegan());
                }
                newRecipe.setVerified(false);
                if(recipeRequest.isGlutenFree() != null) {
                    newRecipe.setGlutenFree(recipeRequest.isGlutenFree());
                }
                if(recipeRequest.isDairyFree() != null) {
                    newRecipe.setDairyFree(recipeRequest.isDairyFree());
                }
                if(recipeRequest.isVeryHealthy() != null) {
                    newRecipe.setVeryHealthy(recipeRequest.isVeryHealthy());
                }
                if(recipeRequest.getCalories() != null) {
                    newRecipe.setCalories(recipeRequest.getCalories());
                }
                if(recipeRequest.getCarbs() != null) {
                    newRecipe.setCarbs(recipeRequest.getCarbs());
                }
                if(recipeRequest.getFat() != null) {
                    newRecipe.setFat(recipeRequest.getFat());
                }
                if(recipeRequest.getProtein() != null) {
                    newRecipe.setProtein(recipeRequest.getProtein());
                }
                newRecipe.setUser(currentUser);
                newRecipe.setCreatedAt();

                if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority(Authority.DIETITIAN.role))) {
                    newRecipe.setVerified(true);
                }
                recipeRepository.save(newRecipe);
                List<Ingredient> newRecipeIngredients = recipeRequest.getRecipesIngredients();

                if(newRecipeIngredients != null) {
                    for (Ingredient ingredient : newRecipeIngredients) {
                        Ingredient newIngredient = new Ingredient();
                        newIngredient.setName(ingredient.getName());
                        newIngredient.setAmount(ingredient.getAmount());
                        newIngredient.setUnit(ingredient.getUnit());
                        newIngredient.setRecipe(newRecipe);

                        ingredientRepository.save(newIngredient);
                    }
                }

                response.setStatus(HttpStatus.CREATED.value());
                return newRecipe;
            }
        }
    }

    public Recipe editRecipe(long recipeId, RecipeRequest editedRecipe, HttpServletResponse response) {
        Recipe existingRecipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id :" + recipeId));

        User currentUser = userDetailsService.findCurrentUser();
        if (currentUser == null){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        } else {
            if(currentUser.getAuthorities().contains(new SimpleGrantedAuthority(Authority.ADMIN.role)) || currentUser == existingRecipe.getUser()) {
                if (editedRecipe.getTitle() != null) {
                    existingRecipe.setTitle(editedRecipe.getTitle());
                }
                if (editedRecipe.getServings() != null) {
                    existingRecipe.setServings(editedRecipe.getServings());
                }
                if (editedRecipe.getReadyInMinutes() != null) {
                    existingRecipe.setReadyInMinutes(editedRecipe.getReadyInMinutes());
                }
                if (editedRecipe.getImagePath() != null) {
                    existingRecipe.setImagePath(editedRecipe.getImagePath());
                }
                if (editedRecipe.getInstructions() != null) {
                    existingRecipe.setInstructions(editedRecipe.getInstructions());
                }
                if (editedRecipe.isVegetarian() != null) {
                    existingRecipe.setVegetarian(editedRecipe.isVegetarian());
                }
                if (editedRecipe.isVegan() != null) {
                    existingRecipe.setVegan(editedRecipe.isVegan());
                }
                if (editedRecipe.isGlutenFree() != null) {
                    existingRecipe.setGlutenFree(editedRecipe.isGlutenFree());
                }
                if (editedRecipe.isDairyFree() != null) {
                    existingRecipe.setDairyFree(editedRecipe.isDairyFree());
                }
                if (editedRecipe.isVeryHealthy() != null) {
                    existingRecipe.setVeryHealthy(editedRecipe.isVeryHealthy());
                }
                if (editedRecipe.isVerified() != null) {
                    existingRecipe.setVerified(editedRecipe.isVerified());
                }
                if (editedRecipe.getCalories() != null) {
                    existingRecipe.setCalories(editedRecipe.getCalories());
                }
                if (editedRecipe.getCarbs() != null) {
                    existingRecipe.setCarbs(editedRecipe.getCarbs());
                }
                if (editedRecipe.getFat() != null) {
                    existingRecipe.setFat(editedRecipe.getFat());
                }
                if (editedRecipe.getProtein() != null) {
                    existingRecipe.setProtein(editedRecipe.getProtein());
                }

                existingRecipe.setCreatedAt();


                if (editedRecipe.getRecipesIngredients() != null) {
                    List<Ingredient> oldIngredients = ingredientRepository.findByrecipe(existingRecipe);
                    ingredientRepository.deleteAll(oldIngredients);

                    List<Ingredient> newIngredients = editedRecipe.getRecipesIngredients();
                    for (Ingredient newIngredient : newIngredients) {
                        newIngredient.setRecipe(existingRecipe);
                        ingredientRepository.save(newIngredient);
                    }
                }
                response.setStatus(HttpStatus.OK.value());
                return recipeRepository.save(existingRecipe);
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return null;
            }
        }
    }

    public Recipe verifyRecipe(Long recipeId, HttpServletResponse response) {
        User currentUser = userDetailsService.findCurrentUser();
        
        if (currentUser == null || !currentUser.getAuthorities().contains(new SimpleGrantedAuthority(Authority.DIETITIAN.role)) ||
                !currentUser.getAuthorities().contains(new SimpleGrantedAuthority(Authority.ADMIN.role))) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            Recipe existingRecipe = recipeRepository.findById(recipeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id :" + recipeId));

            existingRecipe.setVerified(true);
            recipeRepository.save(existingRecipe);
            response.setStatus(HttpStatus.OK.value());
            return existingRecipe;
        }
        return null;
    }

    public void delete(long recipeId, HttpServletResponse response) {
        User currentUser = userDetailsService.findCurrentUser();
        Recipe existingRecipe = recipeRepository.getReferenceById(recipeId);
        if (existingRecipe == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        } else {
            if (currentUser != null){
                User recipeAuthor = existingRecipe.getUser();

                    if(currentUser.getAuthorities().contains(new SimpleGrantedAuthority(Authority.ADMIN.role))){

                        List<Ingredient> existingRecipesIngredients = ingredientRepository.findByrecipe(existingRecipe);
                        for (Ingredient ingredient : existingRecipesIngredients) {
                            ingredientRepository.delete(ingredient);
                        }

                        List<DietDay> dietDays = dayDietRepository.findByRecipesForTodayContaining(existingRecipe);

                        for(DietDay dietDay : dietDays) {
                            DietWeek currentDiet = weekDietRepository.findByDaysForWeekDietContaining(dietDay);
                            if (currentDiet != null) {
                                List<Recipe> recipesInDay = dietDay.getRecipesForToday();
                                if (recipesInDay.contains(existingRecipe)) {
                                    recipesInDay.remove(existingRecipe);
                                    RecipeReplaceRequest recipeReplaceRequest = new RecipeReplaceRequest(dietDay.getId(),
                                            existingRecipe.getId(),
                                            currentDiet.getVegetarian(),
                                            currentDiet.getVegan(),
                                            currentDiet.getGlutenFree(),
                                            currentDiet.getDairyFree(),
                                            currentDiet.getVeryHealthy(),
                                            currentDiet.getVerified(),
                                            excludedProductsListRepository.findByuser(currentDiet.getUser()).getListOfExcludedProducts());
                                    Recipe recipeReplacement = dietService.replaceRecipeFromADay(recipeReplaceRequest, response);
                                    if (recipeReplacement != null) {
                                        recipesInDay.add(recipeReplacement);
                                    }
                                }
                                dietDay.setRecipesForToday(recipesInDay);
                                dayDietRepository.save(dietDay);
                            } else {
                                dayDietRepository.delete(dietDay);
                            }
                        }

                        List<RecipeLike> existingRecipesLikes = recipeLikesRepository.findByrecipe(existingRecipe);
                        for(RecipeLike recipeLike : existingRecipesLikes){
                            recipeLikesRepository.delete(recipeLike);
                        }

                        List<RecipeComment> existingRecipesComments = recipeCommentsRepository.findByrecipe(existingRecipe);
                        for(RecipeComment recipeComment : existingRecipesComments){
                            recipeCommentsRepository.delete(recipeComment);
                        }

                        List<Rating> existingRecipesRatings = ratingRepository.findByrecipe(existingRecipe);
                        for(Rating rating : existingRecipesRatings){
                            ratingRepository.delete(rating);
                        }

                        recipeRepository.delete(existingRecipe);
                        response.setStatus(HttpStatus.ACCEPTED.value());

                    } else {
                        if(recipeAuthor != null) {
                            if(currentUser.getId() == recipeAuthor.getId()) {

                                List<Ingredient> existingRecipesIngredients = ingredientRepository.findByrecipe(existingRecipe);
                                for (Ingredient ingredient : existingRecipesIngredients) {
                                    ingredientRepository.delete(ingredient);
                                }

                                List<DietDay> dietDays = dayDietRepository.findByRecipesForTodayContaining(existingRecipe);

                                for(DietDay dietDay : dietDays) {
                                    DietWeek currentDiet = weekDietRepository.findByDaysForWeekDietContaining(dietDay);
                                    if (currentDiet != null) {
                                        List<Recipe> recipesInDay = dietDay.getRecipesForToday();
                                        if (recipesInDay.contains(existingRecipe)) {
                                            recipesInDay.remove(existingRecipe);
                                            RecipeReplaceRequest recipeReplaceRequest = new RecipeReplaceRequest(dietDay.getId(),
                                                    existingRecipe.getId(),
                                                    currentDiet.getVegetarian(),
                                                    currentDiet.getVegan(),
                                                    currentDiet.getGlutenFree(),
                                                    currentDiet.getDairyFree(),
                                                    currentDiet.getVeryHealthy(),
                                                    currentDiet.getVerified(),
                                                    excludedProductsListRepository.findByuser(currentDiet.getUser()).getListOfExcludedProducts());
                                            Recipe recipeReplacement = dietService.replaceRecipeFromADay(recipeReplaceRequest, response);
                                            if (recipeReplacement != null) {
                                                recipesInDay.add(recipeReplacement);
                                            }
                                        }
                                        dietDay.setRecipesForToday(recipesInDay);
                                        dayDietRepository.save(dietDay);
                                    } else {
                                        dayDietRepository.delete(dietDay);
                                    }
                                }

                                List<RecipeLike> existingRecipesLikes = recipeLikesRepository.findByrecipe(existingRecipe);
                                for(RecipeLike recipeLike : existingRecipesLikes){
                                    recipeLikesRepository.delete(recipeLike);
                                }

                                List<RecipeComment> existingRecipesComments = recipeCommentsRepository.findByrecipe(existingRecipe);
                                for(RecipeComment recipeComment : existingRecipesComments){
                                    recipeCommentsRepository.delete(recipeComment);
                                }

                                List<Rating> existingRecipesRatings = ratingRepository.findByrecipe(existingRecipe);
                                for(Rating rating : existingRecipesRatings){
                                    ratingRepository.delete(rating);
                                }

                                recipeRepository.delete(existingRecipe);
                                response.setStatus(HttpStatus.ACCEPTED.value());

                            } else {
                                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            }
                        } else {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        }
                    }
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            }
        }
    }

    public List<Recipe> getUserRecipes(long userID, HttpServletResponse response) {
        Optional<User> existingUser = userRepository.findById(userID);
        if(existingUser.isPresent()){
            List<Recipe> userRecipes = recipeRepository.findByuser(existingUser.get());
            if(userRecipes.size()>0) {
                List<Recipe> recipeList = new ArrayList<>();
                for (Recipe recipe : userRecipes) {
                    recipeList.add(recipe);
                }
                return recipeList;
            } else {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return null;
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        }
    }
}
