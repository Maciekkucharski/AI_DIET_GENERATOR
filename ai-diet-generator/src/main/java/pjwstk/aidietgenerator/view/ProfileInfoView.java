package pjwstk.aidietgenerator.view;

import pjwstk.aidietgenerator.entity.Gender;

public class ProfileInfoView {
    private Long id;
    private String profilePicturePath;
    private String firstName;
    private String lastName;
    private String email;
    private int age;
    private double weight;
    private int height;
    private Gender gender;
    private double BMI;
    private int kCal;

    public ProfileInfoView(){

    }
    public ProfileInfoView(Long id,
                           String profilePicturePath,
                           String firstName,
                           String lastName,
                           String email,
                           int age,
                           double weight,
                           int height,
                           Gender gender,
                           double BMI,
                           int kCal) {
        this.id = id;
        this.profilePicturePath = profilePicturePath;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.gender = gender;
        this.BMI = BMI;
        this.kCal = kCal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public double getBMI() {
        return BMI;
    }

    public void setBMI(double BMI) {
        this.BMI = BMI;
    }

    public int getkCal() {
        return kCal;
    }

    public void setkCal(int kCal) {
        this.kCal = kCal;
    }
}
