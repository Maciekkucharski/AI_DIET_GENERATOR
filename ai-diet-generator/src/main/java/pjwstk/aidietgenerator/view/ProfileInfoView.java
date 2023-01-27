package pjwstk.aidietgenerator.view;

import pjwstk.aidietgenerator.entity.Gender;

import java.util.List;

public class ProfileInfoView {
    private Long id;
    private String profileImagePath;
    private String firstName;
    private String lastName;
    private String email;
    private int age;
    private double weight;
    private int height;
    private Gender gender;
    private double BMI;
    private int kCal;
    private Boolean completedSurvey;
    private Boolean completedRatings;
    private Boolean subscribed;

    List<String> authorities;

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
                           int kCal,
                           List<String> authorities,
                           Boolean completedSurvey,
                           Boolean completedRatings,
                           Boolean subscribed) {
        this.id = id;
        this.profileImagePath = profilePicturePath;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.gender = gender;
        this.BMI = BMI;
        this.kCal = kCal;
        this.authorities = authorities;
        this.completedRatings = completedRatings;
        this.completedSurvey = completedSurvey;
        this.subscribed = subscribed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
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

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public Boolean getCompletedSurvey() {
        return completedSurvey;
    }

    public void setCompletedSurvey(Boolean completedSurvey) {
        this.completedSurvey = completedSurvey;
    }

    public Boolean getCompletedRatings() {
        return completedRatings;
    }

    public void setCompletedRatings(Boolean completedRatings) {
        this.completedRatings = completedRatings;
    }

    public Boolean getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        this.subscribed = subscribed;
    }
}
