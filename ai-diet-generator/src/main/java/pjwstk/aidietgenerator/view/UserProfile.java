package pjwstk.aidietgenerator.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pjwstk.aidietgenerator.entity.Post;
import pjwstk.aidietgenerator.entity.Socials;
import pjwstk.aidietgenerator.entity.User;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserProfile {
    @JsonIgnoreProperties({"password", "email"})
    private User user;
    private Socials socials;
    private String imagePath;
    private List<Post> userPosts;
}
