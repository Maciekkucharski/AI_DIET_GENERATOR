package pjwstk.aidietgenerator.request;

import pjwstk.aidietgenerator.entity.DietGoal;
import pjwstk.aidietgenerator.entity.PhysicalActivity;
import pjwstk.aidietgenerator.entity.Product;

import java.util.List;

public class DietRequest {
    private PhysicalActivity physicalActivity;
    private DietGoal dietGoal;
    private int mealsPerDay;
    private List<Product> excludedProductsList;
    private Boolean vegetarian;
    private Boolean vegan;
    private Boolean glutenFree;
    private Boolean dairyFree;
    private Boolean veryHealthy;
    private Boolean verified;
    private Boolean personalized;
    private Boolean macroCheck;
    private Double threshold = 0.9;

    public DietRequest(){}

    public DietRequest(PhysicalActivity physicalActivity, DietGoal dietGoal, int mealsPerDay, List<Product> excludedProductsList,
                       Boolean vegetarian, Boolean vegan, Boolean glutenFree, Boolean dairyFree, Boolean veryHealthy, Boolean verified,
                       Boolean personalized, Boolean macroCheck, Double threshold){
        this.physicalActivity = physicalActivity;
        this.dietGoal = dietGoal;
        this.mealsPerDay = mealsPerDay;
        this.excludedProductsList = excludedProductsList;
        this.vegetarian = vegetarian;
        this.vegan = vegan;
        this.glutenFree = glutenFree;
        this.dairyFree = dairyFree;
        this.veryHealthy = veryHealthy;
        this.verified = verified;
        this.personalized = personalized;
        this.macroCheck = macroCheck;
        this.threshold = threshold;
    }

    public PhysicalActivity getPhysicalActivity() {
        return physicalActivity;
    }

    public DietGoal getDietGoal() {
        return dietGoal;
    }

    public int getMealsPerDay() {
        return mealsPerDay;
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

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public Boolean getPersonalized() {
        return personalized;
    }

    public void setPersonalized(Boolean personalized) {
        this.personalized = personalized;
    }

    public Boolean getMacroCheck() {
        return macroCheck;
    }

    public void setMacroCheck(Boolean macroCheck) {
        this.macroCheck = macroCheck;
    }
}
