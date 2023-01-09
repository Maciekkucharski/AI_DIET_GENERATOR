package pjwstk.aidietgenerator.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "saltiness")
    private float saltiness;

    @Column(name = "sourness")
    private float sourness;

    @Column(name = "sweetness")
    private float sweetness;

    @Column(name = "bitterness")
    private float bitterness;

    @Column(name = "spiciness")
    private float spiciness;

    @Column(name = "fattiness")
    private float fattiness;

    @Column(name = "servings")
    private int servings;

    @Column(name = "ready_in_minutes")
    private int readyInMinutes;

    @Column(name = "image")
    private String image;

    @Column(name = "instructions")
    private String instructions;

    @Column(name = "vegetarian")
    private boolean vegetarian;

    @Column(name = "vegan")
    private boolean vegan;

    @Column(name = "gluten_free")
    private boolean glutenFree;

    @Column(name = "dairy_free")
    private boolean dairyFree;

    @Column(name = "very_healthy")
    private boolean veryHealthy;

    @Column(name = "verified")
    private boolean verified;

    @Column(name = "created_at")
    private Timestamp timestamp;

    @Column(name = "calories")
    private int calories;

    @Column(name = "carbs")
    private int carbs;

    @Column(name = "fat")
    private int fat;

    @Column(name = "protein")
    private int protein;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(int readyInMinutes) {
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

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public boolean isVegan() {
        return vegan;
    }

    public void setVegan(boolean vegan) {
        this.vegan = vegan;
    }

    public boolean isGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(boolean glutenFree) {
        this.glutenFree = glutenFree;
    }

    public boolean isDairyFree() {
        return dairyFree;
    }

    public void setDairyFree(boolean dairyFree) {
        this.dairyFree = dairyFree;
    }

    public boolean isVeryHealthy() {
        return veryHealthy;
    }

    public void setVeryHealthy(boolean veryHealthy) {
        this.veryHealthy = veryHealthy;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
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

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public float getSaltiness() {
        return saltiness;
    }

    public void setSaltiness(float saltiness) {
        this.saltiness = saltiness;
    }

    public float getSourness() {
        return sourness;
    }

    public void setSourness(float sourness) {
        this.sourness = sourness;
    }

    public float getSweetness() {
        return sweetness;
    }

    public void setSweetness(float sweetness) {
        this.sweetness = sweetness;
    }

    public float getBitterness() {
        return bitterness;
    }

    public void setBitterness(float bitterness) {
        this.bitterness = bitterness;
    }

    public float getSpiciness() {
        return spiciness;
    }

    public void setSpiciness(float spiciness) {
        this.spiciness = spiciness;
    }

    public float getFattiness() {
        return fattiness;
    }

    public void setFattiness(float fattiness) {
        this.fattiness = fattiness;
    }
}
