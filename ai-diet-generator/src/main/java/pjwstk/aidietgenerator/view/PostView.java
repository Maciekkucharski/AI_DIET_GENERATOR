package pjwstk.aidietgenerator.view;

import java.sql.Timestamp;
import java.util.HashMap;

public class PostView {
    private Long id;
    private String title;
    private String description;
    private Timestamp timestamp;
    private String image_path;
    private HashMap<Long, String> comments;

    public PostView(Long id, String title, String description, Timestamp timestamp, String image_path, HashMap<Long, String> comments) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.image_path = image_path;
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public HashMap<Long, String> getComments() {
        return comments;
    }

    public void setComments(HashMap<Long, String> comments) {
        this.comments = comments;
    }
}
