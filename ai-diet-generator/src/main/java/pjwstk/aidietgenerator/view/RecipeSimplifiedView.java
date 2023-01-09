package pjwstk.aidietgenerator.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pjwstk.aidietgenerator.entity.User;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RecipeSimplifiedView {
    private Long id;
    private String title;
    private Timestamp timestamp;
    @JsonIgnoreProperties({"authorities", "username", "email", "authority"})
    private User author;

    // TODO
    private String userProfilePicture = "TODO";

    private int commentsCount;
    private int likesCount;
}
