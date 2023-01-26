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
    private String profileImagePath;
    private List<Subscription> userSubscriptions;
    @JsonIgnoreProperties({"discord", "facebook", "instagram", "telegram", "facebook", "twitter", "youtube"})
    private UserExtras userExtras;
    private SocialsView socials;
    private List<UserStats> userStats;
    @JsonIgnoreProperties("user")
    private List<PostDetailedView> userPosts;
    @JsonIgnoreProperties("user")
    private List<Recipe> userRecipes;

    private DietGoal dietGoal;
    private Double weightAtDietGeneration;
    private Integer dailyCalGoal;
    private Integer mealsPerDay;

    private ExcludedProductsList excludedProductsList;

}

