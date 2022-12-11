package pjwstk.aidietgenerator.entity;

import javax.persistence.*;

@Entity
@Table(name = "nutritions")
public class Nutrition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "calories")
    private String calories;

    @Column(name = "carbs")
    private String carbs;

    @Column(name = "fat")
    private String fat;

    @Column(name = "protein")
    private String protein;

    @OneToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    public Nutrition(){
    }

    public Nutrition(String calories, String carbs, String fat, String protein, Recipe recipe) {
        this.calories = calories;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
        this.recipe = recipe;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getCarbs() {
        return carbs;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
