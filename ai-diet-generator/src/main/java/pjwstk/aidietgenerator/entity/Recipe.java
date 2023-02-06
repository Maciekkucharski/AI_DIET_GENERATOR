package pjwstk.aidietgenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "recipes")
@Setter
@Getter
@NoArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "saltiness")
    private Float saltiness;

    @Column(name = "sourness")
    private Float sourness;

    @Column(name = "sweetness")
    private Float sweetness;

    @Column(name = "bitterness")
    private Float bitterness;

    @Column(name = "spiciness")
    private Float spiciness;

    @Column(name = "fattiness")
    private Float fattiness;

    @Column(name = "servings")
    private Integer servings;

    @Column(name = "ready_in_minutes")
    private Integer readyInMinutes;

    @Column(name = "image")
    private String imagePath;

    @Column(name = "instructions")
    private String instructions;

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

    @Column(name = "created_at")
    private Timestamp timestamp;

    @Column(name = "calories")
    private Integer calories;

    @Column(name = "carbs")
    private Integer carbs;

    @Column(name = "fat")
    private Integer fat;

    @Column(name = "protein")
    private Integer protein;

    @ManyToOne
    @JsonIgnoreProperties({"email", "authority", "subscribed", "username"})
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> recipesIngredients;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"email", "authority", "subscribed", "username"})
    private List<RecipeLike> recipeLikes;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"email", "authority", "subscribed", "username"})
    private List<RecipeComment>  recipeComments;

    public Recipe(String title, Float saltiness, Float sourness, Float sweetness, Float bitterness, Float spiciness, Float fattiness,
                  Integer servings, Integer readyInMinutes, String image, String instructions,
                  Boolean vegetarian, Boolean vegan, Boolean glutenFree, Boolean dairyFree, Boolean veryHealthy, Boolean verified,
                  Timestamp timestamp, Integer calories, Integer carbs, Integer fat, Integer protein, User user, List<Ingredient> ingredients,
                  List<RecipeLike> recipeLikes, List<RecipeComment> recipeComments) {

        this.title = title;
        this.saltiness = saltiness;
        this.sourness = sourness;
        this.sweetness = sweetness;
        this.bitterness = bitterness;
        this.spiciness = spiciness;
        this.fattiness = fattiness;
        this.servings = servings;
        this.readyInMinutes = readyInMinutes;
        this.imagePath = image;
        this.instructions = instructions;
        this.vegetarian = vegetarian;
        this.vegan = vegan;
        this.glutenFree = glutenFree;
        this.dairyFree = dairyFree;
        this.veryHealthy = veryHealthy;
        this.verified = verified;
        this.timestamp = timestamp;
        this.calories = calories;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
        this.user = user;
        this.recipesIngredients = ingredients;
        this.recipeLikes = recipeLikes;
        this.recipeComments = recipeComments;
    }

    public void setCreatedAt(){
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public String getRecipeCreatorImage() {
        if(user != null) {
            return user.getImagePath();
        }
        return null;
    }
}
