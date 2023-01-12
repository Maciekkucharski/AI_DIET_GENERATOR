package pjwstk.aidietgenerator.request;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {

    private String title;
    private String description;
    private String image_path;


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
