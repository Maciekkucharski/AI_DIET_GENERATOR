package pjwstk.aidietgenerator.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pjwstk.aidietgenerator.entity.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MyProfile {
    @JsonIgnoreProperties({"password"})
    private User user;
    private List<UserStats> userStats;
    private Socials socials;
    private String profileImagePath;
    private List<Post> userPosts;
    private List<Recipe> userRecipes;
    // wykluczenia

    private DietGoal dietGoal;
    private Double weightAtDietGeneration;
    private Integer dailyCalGoal;
    private Integer mealsPerDay;

}

//profil dietetyk: about me, tytuł