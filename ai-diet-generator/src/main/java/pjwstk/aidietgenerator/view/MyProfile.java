package pjwstk.aidietgenerator.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pjwstk.aidietgenerator.entity.Post;
import pjwstk.aidietgenerator.entity.Socials;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.entity.UserStats;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MyProfile {
    @JsonIgnoreProperties({"password"})
    private User user;
    private List<UserStats> userStats;
    private Socials socials;
    private String profilePicturePath;
    private List<Post> userPosts;
}
