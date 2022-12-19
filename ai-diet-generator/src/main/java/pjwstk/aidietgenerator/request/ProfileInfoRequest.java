package pjwstk.aidietgenerator.request;

import pjwstk.aidietgenerator.entity.Gender;

public class ProfileInfoRequest {
    private String profilePicturePath;
    private String firstName;
    private String lastName;
    private String email;
    private double weight;
    private int height;
    private Gender gender;

    public ProfileInfoRequest(){

    }

    public ProfileInfoRequest(String profilePicturePath,
                              String firstName,
                              String lastName,
                              String email,
                              double weight,
                              int height,
                              Gender gender) {

        this.profilePicturePath = profilePicturePath;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.weight = weight;
        this.height = height;
        this.gender = gender;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public double getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    public Gender getGender() {
        return gender;
    }
}
