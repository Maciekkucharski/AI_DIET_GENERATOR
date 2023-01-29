package pjwstk.aidietgenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipes_for_week")
public class DietWeek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST})
    private List<DietDay> daysForWeekDiet;

    @OneToOne
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"firstName", "lastName", "password", "authorities", "email"})
    private User user;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name = "goal")
    private DietGoal dietGoal;
    @Column(name = "start_weight")
    private Double startingWeight;

    @Column(name = "vegetarian")
    private Boolean vegetarian;

    @Column(name = "vegan")
    private Boolean vegan;

    @Column(name = "gluten_free")
    private Boolean glutenFree;

    @Column(name = "dairy_free")
    private Boolean dairyFree;

    @Column(name = "very_healthy")
    private Boolean veryHealthy;

    @Column(name = "verified")
    private Boolean verified;

    public DietWeek(){}

    public DietWeek(List<DietDay> daysForWeekDiet, Timestamp timestamp, User user, DietGoal dietGoal, Double startingWeight,
                    Boolean vegetarian, Boolean vegan, Boolean glutenFree, Boolean dairyFree, Boolean veryHealthy, Boolean verified){
        this.daysForWeekDiet = daysForWeekDiet;
        this.timestamp = timestamp;
        this.user = user;
        this.dietGoal = dietGoal;
        this.startingWeight = startingWeight;
        this.vegetarian = vegetarian;
        this.vegan = vegan;
        this.glutenFree = glutenFree;
        this.dairyFree = dairyFree;
        this.veryHealthy = veryHealthy;
        this.verified = verified;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<DietDay> getDaysForWeekDiet() {
        return daysForWeekDiet;
    }

    public void setDaysForWeekDiet(List<DietDay> daysForWeekDiet) {
        this.daysForWeekDiet = daysForWeekDiet;
    }

    public List<Long> getRecipeIdsForTheWeek(){
        List<Long> thisWeeksRecipesIds = new ArrayList<>();
        for(DietDay oneDay : daysForWeekDiet){
            List<Recipe> oneDaysRecipes = oneDay.getRecipesForToday();
            for(Recipe recipe : oneDaysRecipes){
                if(!thisWeeksRecipesIds.contains(recipe.getId())) {
                    thisWeeksRecipesIds.add(recipe.getId());
                }
            }
        }
        return thisWeeksRecipesIds;
    }

    public void setCreatedAt(){
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public DietGoal getDietGoal() {
        return dietGoal;
    }

    public void setDietGoal(DietGoal dietGoal) {
        this.dietGoal = dietGoal;
    }

    public Double getStartingWeight() {
        return startingWeight;
    }

    public void setStartingWeight(Double startingWeight) {
        this.startingWeight = startingWeight;
    }

    public Boolean getVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(Boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public Boolean getVegan() {
        return vegan;
    }

    public void setVegan(Boolean vegan) {
        this.vegan = vegan;
    }

    public Boolean getGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(Boolean glutenFree) {
        this.glutenFree = glutenFree;
    }

    public Boolean getDairyFree() {
        return dairyFree;
    }

    public void setDairyFree(Boolean dairyFree) {
        this.dairyFree = dairyFree;
    }

    public Boolean getVeryHealthy() {
        return veryHealthy;
    }

    public void setVeryHealthy(Boolean veryHealthy) {
        this.veryHealthy = veryHealthy;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
}
