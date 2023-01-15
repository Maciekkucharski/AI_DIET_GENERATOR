package pjwstk.aidietgenerator.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.sql.Timestamp;

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
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "day_id")
    private DietDay dietDay;

    public Recipe(String title, Float saltiness, Float sourness, Float sweetness, Float bitterness, Float spiciness, Float fattiness, Integer servings, Integer readyInMinutes, String imagePath, String instructions, Boolean vegetarian, Boolean vegan, Boolean glutenFree, Boolean dairyFree, Boolean veryHealthy, Boolean verified, Timestamp timestamp, Integer calories, Integer carbs, Integer fat, Integer protein, User user) {
        this.title = title;
        this.saltiness = saltiness;
        this.sourness = sourness;
        this.sweetness = sweetness;
        this.bitterness = bitterness;
        this.spiciness = spiciness;
        this.fattiness = fattiness;
        this.servings = servings;
        this.readyInMinutes = readyInMinutes;
        this.imagePath = imagePath;
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
    }

    public void setCreatedAt(){
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }
}
