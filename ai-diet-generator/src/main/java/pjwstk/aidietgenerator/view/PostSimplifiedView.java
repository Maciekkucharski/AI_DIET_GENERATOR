package pjwstk.aidietgenerator.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pjwstk.aidietgenerator.entity.User;

import java.sql.Timestamp;

public class PostSimplifiedView {
    private Long id;
    private String title;
    private Timestamp timestamp;
    @JsonIgnoreProperties({"password", "authorities", "username", "email", "enabled", "authority"})
    private User author;
    private int commentsCount;
    private int likesCount;
}
