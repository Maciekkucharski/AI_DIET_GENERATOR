package pjwstk.aidietgenerator.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pjwstk.aidietgenerator.entity.Recipe;
import pjwstk.aidietgenerator.entity.Socials;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.entity.UserExtras;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserProfile {
    @JsonIgnoreProperties({"password", "email", "authority", "authorities", "username"})
    private User user;
    private UserExtras userExtras;
    private Socials socials;
    private int followerCount;
    @JsonIgnoreProperties("user")
    private List<PostDetailedView> userPosts;
    @JsonIgnoreProperties("user")
    private List<Recipe> userRecipes;
}
