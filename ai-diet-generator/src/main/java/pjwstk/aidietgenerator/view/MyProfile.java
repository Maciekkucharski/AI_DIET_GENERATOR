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
    private Socials socials;
    private String profileImagePath;
    private Boolean subscribed;
    private UserExtras userExtras;
    private List<UserStats> userStats;
    @JsonIgnoreProperties("user")
    private List<PostDetailedView> userPosts;
    @JsonIgnoreProperties("user")
    private List<Recipe> userRecipes;
    private ExcludedProductsList excludedProductsList;
}
