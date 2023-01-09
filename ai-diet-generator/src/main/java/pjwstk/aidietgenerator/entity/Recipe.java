package pjwstk.aidietgenerator.entity;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor

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
    private String image;

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

    public void setCreatedAt(){
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }
}
