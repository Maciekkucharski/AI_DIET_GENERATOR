package pjwstk.aidietgenerator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.entity.*;
import pjwstk.aidietgenerator.exception.ResourceNotFoundException;
import pjwstk.aidietgenerator.repository.IngredientRepository;
import pjwstk.aidietgenerator.repository.NutritionRepository;
import pjwstk.aidietgenerator.repository.RecipeRepository;
import pjwstk.aidietgenerator.repository.UserRepository;
import pjwstk.aidietgenerator.request.RecipeRequest;
import pjwstk.aidietgenerator.view.PostView;
import pjwstk.aidietgenerator.view.RecipeView;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final NutritionRepository nutritionRepository;
    private final IngredientRepository ingredientRepository;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository,
                         NutritionRepository nutritionRepository,
                         IngredientRepository ingredientRepository,
                         UserDetailsService userDetailsService,
                         UserRepository userRepository){
        this.recipeRepository = recipeRepository;
        this.nutritionRepository = nutritionRepository;
        this.ingredientRepository = ingredientRepository;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }


    public void addRecipe(RecipeRequest recipeRequest, HttpServletResponse response) {
        User currentUser = userDetailsService.findCurrentUser();
        if(currentUser == null){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            if(recipeRequest.getTitle() == null || recipeRequest.getInstructions() == null || recipeRequest.getIngredients().isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
            } else {
                Recipe newRecipe = new Recipe();
                newRecipe.setTitle(recipeRequest.getTitle());
                newRecipe.setServings(recipeRequest.getServings());
                newRecipe.setReadyInMinutes(recipeRequest.getReadyInMinutes());
                newRecipe.setImage(recipeRequest.getImage());
                newRecipe.setInstructions(recipeRequest.getInstructions());
                newRecipe.setVegetarian(recipeRequest.isVegetarian());
                newRecipe.setVegan(recipeRequest.isVegan());
                newRecipe.setGlutenFree(recipeRequest.isGlutenFree());
                newRecipe.setGlutenFree(recipeRequest.isGlutenFree());
                newRecipe.setDairyFree(recipeRequest.isDairyFree());
                newRecipe.setVeryHealthy(recipeRequest.isVeryHealthy());
                newRecipe.setCreatedAt();
                newRecipe.setUser(currentUser);

                if(currentUser.getAuthorities().contains("ROLE_DIETICIAN")){
                    newRecipe.setVerified(true);
                }
                recipeRepository.save(newRecipe);

                Nutrition newRecipeNutrition = new Nutrition();
                newRecipeNutrition.setCalories(recipeRequest.getCalories());
                newRecipeNutrition.setCarbs(recipeRequest.getCarbs());
                newRecipeNutrition.setFat(recipeRequest.getFat());
                newRecipeNutrition.setProtein(recipeRequest.getProtein());
                newRecipeNutrition.setRecipe(newRecipe);

                nutritionRepository.save(newRecipeNutrition);

                List<Ingredient> newRecipeIngredients = recipeRequest.getIngredients();

                for(Ingredient ingredient : newRecipeIngredients) {
                    Ingredient newIngredient = new Ingredient();
                    newIngredient.setName(ingredient.getName());
                    newIngredient.setAmount(ingredient.getAmount());
                    newIngredient.setUnit(ingredient.getUnit());
                    newIngredient.setRecipe(newRecipe);

                    ingredientRepository.save(newIngredient);
                }

                response.setStatus(HttpStatus.CREATED.value());
            }
        }
    }

    public RecipeView view(long recipeId, HttpServletResponse response) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if (recipe.isEmpty()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        } else {
            List ingredients = new ArrayList<Ingredient>();
            Nutrition nutrition = new Nutrition();
            try {
                ingredients = ingredientRepository.findByrecipe(recipe.get());
            } catch (NoResultException e) {
                e.printStackTrace();
            }
            try {
                nutrition = nutritionRepository.findByrecipe(recipe.get());
            } catch (NoResultException e) {
                e.printStackTrace();
            }

            response.setStatus(HttpStatus.OK.value());
            return new RecipeView(recipe.get().getId(),
                    recipe.get().getTitle(),
                    recipe.get().getServings(),
                    recipe.get().getReadyInMinutes(),
                    recipe.get().getImage(),
                    recipe.get().getInstructions(),
                    recipe.get().isVegetarian(),
                    recipe.get().isVegan(),
                    recipe.get().isGlutenFree(),
                    recipe.get().isDairyFree(),
                    recipe.get().isVeryHealthy(),
                    recipe.get().isVerified(),
                    recipe.get().getTimestamp(),
                    recipe.get().getUser(),
                    nutrition.getCalories(),
                    nutrition.getCarbs(),
                    nutrition.getFat(),
                    nutrition.getProtein(),
                    ingredients);
        }
    }

    public Recipe editRecipe(long recipeId, Recipe editedRecipe, HttpServletResponse response) {
        Recipe existingRecipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id :" + recipeId));

        User currentUser = userDetailsService.findCurrentUser();
        if (currentUser == null
                || !currentUser.getAuthorities().contains("ROLE_DIETICIAN")
                || !currentUser.getAuthorities().contains("ROLE_ADMIN")
                || currentUser != existingRecipe.getUser()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;

        } else {
            existingRecipe = editedRecipe;
            recipeRepository.save(existingRecipe);
            response.setStatus(HttpStatus.OK.value());
            return existingRecipe;
        }
    }

    public Recipe verifyRecipe(Long recipeId, HttpServletResponse response) {
        User currentUser = userDetailsService.findCurrentUser();
        if(currentUser == null || !currentUser.getAuthorities().contains("ROLE_DIETICIAN") || !currentUser.getAuthorities().contains("ROLE_ADMIN")) {
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
            if (currentUser != null && (currentUser.getId() == existingRecipe.getUser().getId() || currentUser.getAuthorities().contains("ROLE_ADMIN"))) {
                recipeRepository.delete(existingRecipe);
                response.setStatus(HttpStatus.ACCEPTED.value());
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            }
        }
    }
}
