package pjwstk.aidietgenerator.request;

import pjwstk.aidietgenerator.entity.DietGoal;
import pjwstk.aidietgenerator.entity.ExcludedProductsList;
import pjwstk.aidietgenerator.entity.PhysicalActivity;

public class DietRequest {
    private PhysicalActivity physicalActivity;
    private DietGoal dietGoal;
    private int mealsPerDay;
    ExcludedProductsList excludedProductsList;

    public DietRequest(){}

    public DietRequest(PhysicalActivity physicalActivity, DietGoal dietGoal, int mealsPerDay, ExcludedProductsList excludedProductsList){
        this.physicalActivity = physicalActivity;
        this.dietGoal = dietGoal;
        this.mealsPerDay = mealsPerDay;
        this.excludedProductsList = excludedProductsList;
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

    public ExcludedProductsList getExcludedProductsList() {
        return excludedProductsList;
    }
}
