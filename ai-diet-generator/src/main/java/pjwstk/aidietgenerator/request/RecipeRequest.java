package pjwstk.aidietgenerator.request;

import pjwstk.aidietgenerator.entity.Ingredient;
import pjwstk.aidietgenerator.entity.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RecipeRequest {

    //    Recipe
    private String title;
    private int servings;
    private int readyInMinutes;
    private String image;
    private String instructions;
    private boolean vegetarian;
    private boolean vegan;
    private boolean glutenFree;
    private boolean dairyFree;
    private boolean veryHealthy;
    private boolean verified;
    private Timestamp created_at;
    private User user;
    //    Nutrition
    private int calories;
    private int carbs;
    private int fat;
    private int protein;
    //    Ingredients
    private List<Ingredient> ingredients;

    public RecipeRequest(long id, String title,
                      int servings,
                      int readyInMinutes,
                      String image,
                      String instructions,
                      boolean vegetarian, boolean vegan, boolean glutenFree, boolean dairyFree, boolean veryHealthy, boolean verified,
                      Timestamp created_at,
                      User user,
                      int calories,
                      int carbs,
                      int fat,
                      int protein,
                      Ingredient ... ingredients) {
        this.title = title;
        this.servings = servings;
        this.readyInMinutes = readyInMinutes;
        this.image = image;
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

        List<Ingredient> ingredientList = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            ingredientList.add(ingredient);
        }

        this.ingredients = ingredientList;
    }

    public String getTitle() {
        return title;
    }

    public int getServings() {
        return servings;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public String getImage() {
        return image;
    }

    public String getInstructions() {
        return instructions;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public boolean isVegan() {
        return vegan;
    }

    public boolean isGlutenFree() {
        return glutenFree;
    }

    public boolean isDairyFree() {
        return dairyFree;
    }

    public boolean isVeryHealthy() {
        return veryHealthy;
    }

    public boolean isVerified() {
        return verified;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public User getUser() {
        return user;
    }

    public int getCalories() {
        return calories;
    }

    public int getCarbs() {
        return carbs;
    }

    public int getFat() {
        return fat;
    }

    public int getProtein() {
        return protein;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}
