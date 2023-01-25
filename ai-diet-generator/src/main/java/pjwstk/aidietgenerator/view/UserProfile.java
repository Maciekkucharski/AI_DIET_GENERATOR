package pjwstk.aidietgenerator.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pjwstk.aidietgenerator.entity.Follow;
import pjwstk.aidietgenerator.entity.Recipe;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.entity.UserExtras;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserProfile {
    @JsonIgnoreProperties({"password", "email", "authority", "authorities", "username" ,"subscribed"})
    private User user;
    private String userImagePath;
    @JsonIgnoreProperties({"discord", "facebook", "instagram", "telegram", "facebook", "twitter", "youtube"})
    private UserExtras userExtras;
    private SocialsView socials;
    @JsonIgnoreProperties({"user"})
    private List<Follow> followingList;
    @JsonIgnoreProperties({"author", "user"})
    private List<PostDetailedView> userPosts;
    @JsonIgnoreProperties({"author", "user"})
    private List<Recipe> userRecipes;
}
