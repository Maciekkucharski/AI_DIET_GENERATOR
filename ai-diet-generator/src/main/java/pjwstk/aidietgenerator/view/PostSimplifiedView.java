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

@NoArgsConstructor
@Setter
@Getter
public class PostSimplifiedView {
    private Long id;
    private String title;
    private String description;
    private Timestamp timestamp;
    @JsonIgnoreProperties({"authorities", "username", "email", "authority"})
    private User author;
    private String userProfilePicture;
    private String postImagePath;
    private List<PostLike> postLikes;
    private List<PostComment> postComments;
}
