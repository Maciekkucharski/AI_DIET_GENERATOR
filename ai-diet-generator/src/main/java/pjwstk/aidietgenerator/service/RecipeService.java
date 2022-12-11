package pjwstk.aidietgenerator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.entity.Recipe;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.exception.ResourceNotFoundException;
import pjwstk.aidietgenerator.repository.IngredientRepository;
import pjwstk.aidietgenerator.repository.NutritionRepository;
import pjwstk.aidietgenerator.repository.RecipeRepository;
import pjwstk.aidietgenerator.repository.UserRepository;

import javax.servlet.http.HttpServletResponse;

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


    public Recipe addRecipe(Recipe recipe, HttpServletResponse response) {
        User currentUser = userDetailsService.findCurrentUser();
        if(currentUser == null){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            if(recipe.getTitle() == null || recipe.getInstructions() == null) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
            } else {
                Recipe newRecipe = new Recipe();
                newRecipe.setId(recipe.getId());
                newRecipe.setTitle(recipe.getTitle());
                newRecipe.setServings(recipe.getServings());
                newRecipe.setReadyInMinutes(recipe.getReadyInMinutes());
                newRecipe.setImage(recipe.getImage());
                newRecipe.setInstructions(recipe.getInstructions());
                newRecipe.setVegetarian(recipe.isVegetarian());
                newRecipe.setVegan(recipe.isVegan());
                newRecipe.setGlutenFree(recipe.isGlutenFree());
                newRecipe.setCreatedAt();
                newRecipe.setUser(currentUser);
                response.setStatus(HttpStatus.CREATED.value());
                return recipeRepository.save(newRecipe);
            }
        }
        return null;
    }
//
//    public Recipe editRecipe(long recipeId, Recipe recipe, HttpServletResponse response) {
//        User currentUser = userDetailsService.findCurrentUser();
//        if(currentUser == null || !currentUser.getAuthorities().contains("ROLE_DIETICIAN") || !currentUser.getAuthorities().contains("ROLE_ADMIN")) {
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        } else {
//            Recipe existingRecipe = recipeRepository.findById(recipeId)
//                    .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id :" + recipeId));
//            Recipe newRecipe = new Recipe();
//            newRecipe.setId(recipe.getId());
//            newRecipe.setTitle(recipe.getTitle());
//            newRecipe.setServings(recipe.getServings());
//            newRecipe.setReadyInMinutes(recipe.getReadyInMinutes());
//            newRecipe.setImage(recipe.getImage());
//            newRecipe.setInstructions(recipe.getInstructions());
//            newRecipe.setVegetarian(recipe.isVegetarian());
//            newRecipe.setVegan(recipe.isVegan());
//            newRecipe.setGlutenFree(recipe.isGlutenFree());
//            newRecipe.setCreatedAt();
//            newRecipe.setUser(currentUser);
//
//    }

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
