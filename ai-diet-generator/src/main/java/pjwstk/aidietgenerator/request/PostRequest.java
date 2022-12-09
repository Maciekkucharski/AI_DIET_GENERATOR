package pjwstk.aidietgenerator.request;

public class PostRequest {

    private String title;
    private String description;
    private String image_path;

    public PostRequest(String title, String description, String image_path) {
        this.title = title;
        this.description = description;
        this.image_path = image_path;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage_path() {
        return image_path;
    }
}
