package pjwstk.aidietgenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pjwstk.aidietgenerator.entity.Recipe;
import pjwstk.aidietgenerator.exception.ResourceNotFoundException;
import pjwstk.aidietgenerator.repository.IngredientRepository;
import pjwstk.aidietgenerator.repository.RecipeRepository;
import pjwstk.aidietgenerator.request.RecipeRequest;
import pjwstk.aidietgenerator.service.RecipeService;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/recipe")
@CrossOrigin(exposedHeaders = "*")
public class RecipeController {

    private final RecipeService recipeService;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public RecipeController(RecipeService recipeService, RecipeRepository recipeRepository, IngredientRepository ingredientRepository){
        this.recipeService = recipeService;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping("/{id}")
    public Recipe findRecipeById(@PathVariable(value = "id") long recipeId){
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id :" + recipeId));
    }

    @DeleteMapping("/{id}")
    public void deleteRecipeById(@PathVariable(value = "id") long recipeId, HttpServletResponse response){
        recipeService.delete(recipeId, response);
    }

    @PutMapping("/{id}")
    public Recipe updatedExistingRecipe(@PathVariable(value = "id") long recipeId,@RequestBody RecipeRequest recipeRequest, HttpServletResponse response){
        return recipeService.editRecipe(recipeId, recipeRequest, response);
    }

    @GetMapping("/user/{userID}")
    public List<Recipe> findUserRecipes(@PathVariable(value = "userID") long userID, HttpServletResponse response){
        return recipeService.getUserRecipes(userID, response);
    }

    @PostMapping("/add")
    @Transactional
    public Recipe addRecipe(@RequestBody RecipeRequest recipeRequest, HttpServletResponse response){
        return recipeService.addRecipe(recipeRequest, response);
    }

    @GetMapping("/all")
    public List<Recipe> getAllRecipes(){
        return recipeRepository.findAll();
    }

    @GetMapping("/verify/{recipeID}")
    public void verifyRecipe(@PathVariable(value = "recipeID") long recipeId, HttpServletResponse response){
        recipeService.verifyRecipe(recipeId, response);
    }

}
