package pjwstk.aidietgenerator.request;

import lombok.NoArgsConstructor;
import pjwstk.aidietgenerator.entity.Ingredient;
import pjwstk.aidietgenerator.entity.User;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
public class RecipeRequest {
    private String title;
    private Integer servings;
    private Integer readyInMinutes;
    private String imagePath;
    private String instructions;
    private Boolean vegetarian;
    private Boolean vegan;
    private Boolean glutenFree;
    private Boolean dairyFree;
    private Boolean veryHealthy;
    private Boolean verified;
    private Timestamp created_at;
    private User user;
    private Integer calories;
    private Integer carbs;
    private Integer fat;
    private Integer protein;
    private List<Ingredient> recipesIngredients;

    public RecipeRequest(String title,
                      Integer servings,
                      Integer readyInMinutes,
                      String imagePath,
                      String instructions,
                      Boolean vegetarian, Boolean vegan, Boolean glutenFree, Boolean dairyFree, Boolean veryHealthy, Boolean verified,
                      Timestamp created_at,
                      User user,
                      Integer calories,
                      Integer carbs,
                      Integer fat,
                      Integer protein,
                      List<Ingredient> recipesIngredients) {
        this.title = title;
        this.servings = servings;
        this.readyInMinutes = readyInMinutes;
        this.imagePath = imagePath;
        this.instructions = instructions;
        this.vegetarian = vegetarian;
        this.vegan = vegan;
        this.glutenFree = glutenFree;
        this.dairyFree = dairyFree;
        this.veryHealthy = veryHealthy;
        this.verified = verified;
        this.created_at = created_at;
        this.user = user;
        this.calories = calories;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
        this.recipesIngredients = recipesIngredients;
    }

    public String getTitle() {
        return title;
    }

    public Integer getServings() {
        return servings;
    }

    public Integer getReadyInMinutes() {
        return readyInMinutes;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getInstructions() {
        return instructions;
    }

    public Boolean isVegetarian() {
        return vegetarian;
    }

    public Boolean isVegan() {
        return vegan;
    }

    public Boolean isGlutenFree() {
        return glutenFree;
    }

    public Boolean isDairyFree() {
        return dairyFree;
    }

    public Boolean isVeryHealthy() {
        return veryHealthy;
    }

    public Boolean isVerified() {
        return verified;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public User getUser() {
        return user;
    }

    public Integer getCalories() {
        return calories;
    }

    public Integer getCarbs() {
        return carbs;
    }

    public Integer getFat() {
        return fat;
    }

    public Integer getProtein() {
        return protein;
    }

    public List<Ingredient> getRecipesIngredients() {
        return recipesIngredients;
    }


}
