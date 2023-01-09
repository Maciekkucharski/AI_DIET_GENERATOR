package pjwstk.aidietgenerator.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pjwstk.aidietgenerator.entity.PostComment;
import pjwstk.aidietgenerator.entity.PostLike;
import pjwstk.aidietgenerator.entity.User;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostDetailedView {
    private Long id;
    private String title;
    private String description;
    private Timestamp timestamp;

    // TODO
    private String imagePath;

    @JsonIgnoreProperties({"authorities", "username", "email", "authority"})
    private User author;

    // TODO
    private String userProfilePicture;

    private List<PostComment> postComments;
    private List<PostLike> likes;
}
