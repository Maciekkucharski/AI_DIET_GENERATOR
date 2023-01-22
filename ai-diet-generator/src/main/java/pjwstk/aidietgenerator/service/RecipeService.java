package pjwstk.aidietgenerator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.entity.*;
import pjwstk.aidietgenerator.exception.ResourceNotFoundException;
import pjwstk.aidietgenerator.repository.IngredientRepository;
import pjwstk.aidietgenerator.repository.RecipeRepository;
import pjwstk.aidietgenerator.repository.UserRepository;
import pjwstk.aidietgenerator.request.RecipeRequest;
import pjwstk.aidietgenerator.view.RecipeView;

import javax.persistence.NoResultException;
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

    @Autowired
    public RecipeService(RecipeRepository recipeRepository,
                         IngredientRepository ingredientRepository,
                         UserDetailsService userDetailsService,
                         UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }


    public Recipe addRecipe(RecipeRequest recipeRequest, HttpServletResponse response) {
        User currentUser = userDetailsService.findCurrentUser();
        if (currentUser == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        } else {
            if (recipeRequest.getTitle() == null || recipeRequest.getInstructions() == null || recipeRequest.getIngredients().isEmpty()) {
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

                if (currentUser.getAuthorities().contains("ROLE_DIETITIAN")) {
                    newRecipe.setVerified(true);
                }
                recipeRepository.save(newRecipe);
                List<Ingredient> newRecipeIngredients = recipeRequest.getIngredients();

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

    public RecipeView view(long recipeId, HttpServletResponse response) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);

        if (recipe.isEmpty()) {

            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;

        } else {

            List ingredients = new ArrayList<Ingredient>();

            try {
                ingredients = ingredientRepository.findByrecipe(recipe.get());
            } catch (NoResultException e) {
                e.printStackTrace();
            }

            RecipeView currentRecipeView = new RecipeView();

            response.setStatus(HttpStatus.OK.value());

            if(recipe.get().getId() != null) {
                currentRecipeView.setId(recipe.get().getId());
            }
            if(recipe.get().getTitle() != null){
                currentRecipeView.setTitle(recipe.get().getTitle());
            }
            if(recipe.get().getReadyInMinutes() != null){
                currentRecipeView.setReadyInMinutes(recipe.get().getReadyInMinutes());
            }
            if(recipe.get().getImagePath() != null){
                currentRecipeView.setImagePath(recipe.get().getImagePath());
            }
            if(recipe.get().getInstructions() != null){
                currentRecipeView.setInstructions(recipe.get().getInstructions());
            }
            if(recipe.get().getVegetarian() != null){
                currentRecipeView.setVegetarian(recipe.get().getVegetarian());
            }
            if(recipe.get().getVegan() != null){
                currentRecipeView.setVegan(recipe.get().getVegan());
            }
            if(recipe.get().getGlutenFree() != null){
                currentRecipeView.setGlutenFree(recipe.get().getGlutenFree());
            }
            if(recipe.get().getDairyFree() != null){
                currentRecipeView.setDairyFree(recipe.get().getDairyFree());
            }
            if(recipe.get().getVeryHealthy() != null){
                currentRecipeView.setVeryHealthy(recipe.get().getVeryHealthy());
            }
            if(recipe.get().getVerified() != null){
                currentRecipeView.setVerified(recipe.get().getVerified());
            }
            if(recipe.get().getTimestamp() != null){
                currentRecipeView.setCreated_at(recipe.get().getTimestamp());
            }
            if(recipe.get().getUser() != null){
                currentRecipeView.setUser(recipe.get().getUser());
            }
            if(recipe.get().getCalories() != null){
                currentRecipeView.setCalories(recipe.get().getCalories());
            }
            if(recipe.get().getCarbs() != null){
                currentRecipeView.setCarbs(recipe.get().getCarbs());
            }
            if(recipe.get().getFat() != null){
                currentRecipeView.setFat(recipe.get().getFat());
            }
            if(recipe.get().getProtein() != null){
                currentRecipeView.setProtein(recipe.get().getProtein());
            }
            if(ingredients != null){
                currentRecipeView.setIngredients(ingredients);
            }


            return currentRecipeView;
        }
    }

    public Recipe editRecipe(long recipeId, RecipeRequest editedRecipe, HttpServletResponse response) {
        Recipe existingRecipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id :" + recipeId));

        User currentUser = userDetailsService.findCurrentUser();
        if (currentUser == null || (!currentUser.getAuthorities().contains("ROLE_DIETICIAN") && !currentUser.getAuthorities().contains("ROLE_ADMIN") && currentUser != existingRecipe.getUser())) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;

        } else {
            if(editedRecipe.getTitle() != null) {
                existingRecipe.setTitle(editedRecipe.getTitle());
            }
            if(editedRecipe.getServings() != null) {
                existingRecipe.setServings(editedRecipe.getServings());
            }
            if(editedRecipe.getReadyInMinutes() != null) {
                existingRecipe.setReadyInMinutes(editedRecipe.getReadyInMinutes());
            }
            if(editedRecipe.getImagePath() != null) {
                existingRecipe.setImagePath(editedRecipe.getImagePath());
            }
            if(editedRecipe.getInstructions() != null) {
                existingRecipe.setInstructions(editedRecipe.getInstructions());
            }
            if(editedRecipe.isVegetarian() != null) {
                existingRecipe.setVegetarian(editedRecipe.isVegetarian());
            }
            if(editedRecipe.isVegan() != null) {
                existingRecipe.setVegan(editedRecipe.isVegan());
            }
            if(editedRecipe.isGlutenFree() != null) {
                existingRecipe.setGlutenFree(editedRecipe.isGlutenFree());
            }
            if(editedRecipe.isDairyFree() != null) {
                existingRecipe.setDairyFree(editedRecipe.isDairyFree());
            }
            if(editedRecipe.isVeryHealthy() != null) {
                existingRecipe.setVeryHealthy(editedRecipe.isVeryHealthy());
            }
            if(editedRecipe.isVerified() != null) {
                existingRecipe.setVerified(editedRecipe.isVerified());
            }
            if(editedRecipe.getCalories() != null) {
                existingRecipe.setCalories(editedRecipe.getCalories());
            }
            if(editedRecipe.getCarbs() != null) {
                existingRecipe.setCarbs(editedRecipe.getCarbs());
            }
            if(editedRecipe.getFat() != null) {
                existingRecipe.setFat(editedRecipe.getFat());
            }
            if(editedRecipe.getProtein() != null) {
                existingRecipe.setProtein(editedRecipe.getProtein());
            }

            existingRecipe.setCreatedAt();


            if(editedRecipe.getIngredients() != null) {
                List<Ingredient> newIngredients = editedRecipe.getIngredients();
                for (Ingredient newIngredient : newIngredients) {
                    newIngredient.setRecipe(existingRecipe);
                    ingredientRepository.save(newIngredient);
                }
                List<Ingredient> oldIngredients = ingredientRepository.findByrecipe(existingRecipe);
                ingredientRepository.deleteAll(oldIngredients);
            }
            response.setStatus(HttpStatus.OK.value());
            return recipeRepository.save(existingRecipe);
        }
    }

    public Recipe verifyRecipe(Long recipeId, HttpServletResponse response) {
        User currentUser = userDetailsService.findCurrentUser();
        if (currentUser == null || !currentUser.getAuthorities().contains("ROLE_DIETICIAN") || !currentUser.getAuthorities().contains("ROLE_ADMIN")) {
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
                List<Ingredient> existingRecipesIngredients = ingredientRepository.findByrecipe(existingRecipe);
                for (Ingredient ingredient : existingRecipesIngredients) {
                    ingredientRepository.delete(ingredient);
                }
                recipeRepository.delete(existingRecipe);
                response.setStatus(HttpStatus.ACCEPTED.value());
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            }
        }
    }
}
