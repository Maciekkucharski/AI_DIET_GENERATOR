package pjwstk.aidietgenerator.request;

import pjwstk.aidietgenerator.entity.Gender;

public class ProfileInfoRequest {
    private String profilePicturePath;
    private String firstName;
    private String lastName;

    private int age;
    private String email;
    private double weight;
    private int height;
    private String genderName;

    public ProfileInfoRequest(){

    }

    public ProfileInfoRequest(String profilePicturePath,
                              String firstName,
                              String lastName,
                              int age,
                              String email,
                              double weight,
                              int height,
                              String genderName) {

        this.profilePicturePath = profilePicturePath;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.weight = weight;
        this.height = height;
        this.genderName = genderName;
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

    public String getGender() {
        return genderName;
    }

    public int getAge() {
        return age;
    }
}
