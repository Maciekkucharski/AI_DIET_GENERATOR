package pjwstk.aidietgenerator.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostRequest {
    private String title;
    private String description;
    private String imagePath;
}
