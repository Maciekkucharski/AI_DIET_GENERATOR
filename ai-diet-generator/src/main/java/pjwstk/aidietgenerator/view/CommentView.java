package pjwstk.aidietgenerator.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pjwstk.aidietgenerator.entity.User;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CommentView {
    private Long id;
    private String content;
    private Timestamp timestamp;
    @JsonIgnoreProperties({"password", "authorities", "username", "email", "authority"})
    private User user;
    private String userImagePath;
}
