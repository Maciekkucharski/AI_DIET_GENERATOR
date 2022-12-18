package pjwstk.aidietgenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserProfile {
    @JsonIgnoreProperties({"password", "email"})
    private User user;
    private Socials socials;
    private String profilePicturePath;
    private List<Post> userPosts;
}
