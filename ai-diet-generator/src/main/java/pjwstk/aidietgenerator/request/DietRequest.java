package pjwstk.aidietgenerator.request;

import pjwstk.aidietgenerator.entity.DietGoal;
import pjwstk.aidietgenerator.entity.ExcludedProductsList;
import pjwstk.aidietgenerator.entity.PhysicalActivity;
import pjwstk.aidietgenerator.entity.Product;

import java.util.List;

public class DietRequest {
    private PhysicalActivity physicalActivity;
    private DietGoal dietGoal;
    private int mealsPerDay;
    private List<Product> excludedProductsList;

    public DietRequest(){}

    public DietRequest(PhysicalActivity physicalActivity, DietGoal dietGoal, int mealsPerDay, List<Product> excludedProductsList){
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

    public List<Product> getExcludedProductsList() {
        return excludedProductsList;
    }
}
