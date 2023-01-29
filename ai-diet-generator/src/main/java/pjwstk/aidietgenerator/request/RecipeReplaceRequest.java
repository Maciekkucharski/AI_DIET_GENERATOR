package pjwstk.aidietgenerator.request;

import pjwstk.aidietgenerator.entity.Product;

import java.util.List;

public class RecipeReplaceRequest {
    private Long dayDietId;
    private Long recipeToReplaceId;
    private List<Product> excludedProductsList;
    private Boolean vegetarian;
    private Boolean vegan;
    private Boolean glutenFree;
    private Boolean dairyFree;
    private Boolean veryHealthy;
    private Boolean verified;


    public RecipeReplaceRequest(){}

    public RecipeReplaceRequest(Long dayDietId, Long recipeToReplaceId){
        this.dayDietId = dayDietId;
        this.recipeToReplaceId = recipeToReplaceId;
    }

    public RecipeReplaceRequest(Long dayDietId, Long recipeToReplaceId,
                                Boolean vegetarian, Boolean vegan, Boolean glutenFree, Boolean dairyFree, Boolean veryHealthy, Boolean verified,
                                List<Product> excludedProductsList){
        this.dayDietId = dayDietId;
        this.recipeToReplaceId = recipeToReplaceId;
        this.vegetarian = vegetarian;
        this.vegan = vegan;
        this.glutenFree = glutenFree;
        this.dairyFree = dairyFree;
        this.veryHealthy = veryHealthy;
        this.verified = verified;
        this.excludedProductsList = excludedProductsList;
    }

    public Long getDayDietId() {
        return dayDietId;
    }

    public Long getRecipeToReplaceId() {
        return recipeToReplaceId;
    }

    public List<Product> getExcludedProductsList() {
        return excludedProductsList;
    }

    public Boolean getVegetarian() {
        return vegetarian;
    }

    public Boolean getVegan() {
        return vegan;
    }

    public Boolean getGlutenFree() {
        return glutenFree;
    }

    public Boolean getDairyFree() {
        return dairyFree;
    }

    public Boolean getVeryHealthy() {
        return veryHealthy;
    }

    public Boolean getVerified() {
        return verified;
    }
}
