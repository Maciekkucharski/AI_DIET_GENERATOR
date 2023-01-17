package pjwstk.aidietgenerator.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipes_for_day")
public class DietDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "week_id")
    private DietWeek dietWeek;

    @OneToMany
    @JsonIgnoreProperties({"saltiness", "sourness", "sweetness", "bitterness", "fattiness", "spiciness", "instructions", "timestamp", "user"})
    private List<Recipe> recipesForToday;

    public DietDay(){}

    public DietDay(List<Recipe> recipesForToday, DietWeek dietWeek)
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

    public DietWeek getDietWeek() {
        return dietWeek;
    }

    public void setDietWeek(DietWeek dietWeek) {
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
            todaysIds.add(recipe.getId());
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
}
