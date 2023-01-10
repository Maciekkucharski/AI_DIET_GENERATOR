package pjwstk.aidietgenerator.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "recipes")
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

    public Recipe(){
    }

    public Recipe(String title, int servings, int readyInMinutes, String image, String instructions,
                  boolean vegetarian, boolean vegan, boolean glutenFree, boolean dairyFree, boolean veryHealthy, boolean verified,
                  int calories, int carbs, int fat, int protein) {
        this.title = title;
        this.servings = servings;
        this.readyInMinutes = readyInMinutes;
        this.image = image;
        this.instructions = instructions;
        this.vegetarian = vegetarian;
        this.vegan = vegan;
        this.glutenFree = glutenFree;
        this.dairyFree = dairyFree;
        this.veryHealthy  = veryHealthy;
        this.verified = verified;
        this.calories = calories;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getSaltiness() {
        return saltiness;
    }

    public void setSaltiness(Float saltiness) {
        this.saltiness = saltiness;
    }

    public Float getSourness() {
        return sourness;
    }

    public void setSourness(Float sourness) {
        this.sourness = sourness;
    }

    public Float getSweetness() {
        return sweetness;
    }

    public void setSweetness(Float sweetness) {
        this.sweetness = sweetness;
    }

    public Float getBitterness() {
        return bitterness;
    }

    public void setBitterness(Float bitterness) {
        this.bitterness = bitterness;
    }

    public Float getSpiciness() {
        return spiciness;
    }

    public void setSpiciness(Float spiciness) {
        this.spiciness = spiciness;
    }

    public Float getFattiness() {
        return fattiness;
    }

    public void setFattiness(Float fattiness) {
        this.fattiness = fattiness;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public Integer getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(Integer readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
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

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Integer getCarbs() {
        return carbs;
    }

    public void setCarbs(Integer carbs) {
        this.carbs = carbs;
    }

    public Integer getFat() {
        return fat;
    }

    public void setFat(Integer fat) {
        this.fat = fat;
    }

    public Integer getProtein() {
        return protein;
    }

    public void setProtein(Integer protein) {
        this.protein = protein;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setCreatedAt(){
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

}
