package pjwstk.aidietgenerator.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipes_for_day")
public class DietDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST})
    @JsonIgnore
    @JoinColumn(name = "week_id")
    private List<DietWeek> dietWeek;

    @ManyToMany
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST})
    @JsonIgnoreProperties({"saltiness", "sourness", "sweetness", "bitterness", "fattiness", "spiciness", "timestamp", "user"})
    private List<Recipe> recipesForToday;

    public DietDay(){}

    public DietDay(List<Recipe> recipesForToday, List<DietWeek> dietWeek)
    {
        this.recipesForToday = recipesForToday;
        this.dietWeek = dietWeek;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<DietWeek> getDietWeek() {
        return dietWeek;
    }

    public void setDietWeek(List<DietWeek> dietWeek) {
        this.dietWeek = dietWeek;
    }

    public List<Recipe> getRecipesForToday() {
        return recipesForToday;
    }

    public void setRecipesForToday(List<Recipe> recipesForToday) {
        this.recipesForToday = recipesForToday;
    }

    public List<Long> getTodaysRecipesIds(){
        List<Recipe> todaysRecipes = getRecipesForToday();
        List<Long> todaysIds = new ArrayList<>();

        for(Recipe recipe : todaysRecipes){
            if(!todaysIds.contains(recipe.getId())) {
                todaysIds.add(recipe.getId());
            }
        }

        return todaysIds;
    }

    public double getTodaysCalories(){
        List<Recipe> todaysRecipes = getRecipesForToday();
        double calories = 0;
        for(Recipe recipe : todaysRecipes){
            calories += recipe.getCalories();
        }
        return calories;
    }

    public double getTodaysCarbs(){
        List<Recipe> todaysRecipes = getRecipesForToday();
        int carbs = 0;
        for(Recipe recipe : todaysRecipes){
            carbs += recipe.getCarbs();
        }
        return carbs;
    }
    public double getTodaysFat(){
        List<Recipe> todaysRecipes = getRecipesForToday();
        int fat = 0;
        for(Recipe recipe : todaysRecipes){
            fat += recipe.getFat();
        }
        return fat;
    }
    public double getTodaysProtein(){
        List<Recipe> todaysRecipes = getRecipesForToday();
        int protein = 0;
        for(Recipe recipe : todaysRecipes){
            protein += recipe.getProtein();
        }
        return protein;
    }
}
