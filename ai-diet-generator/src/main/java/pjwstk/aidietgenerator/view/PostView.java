package pjwstk.aidietgenerator.view;

import java.sql.Timestamp;
import java.util.HashMap;

public class PostView {
    private Long id;
    private String title;
    private String description;
    private Timestamp timestamp;
    private HashMap<Long, String> comments;

    public PostView(Long id, String title, String description, Timestamp timestamp, HashMap<Long, String> comments) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
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
