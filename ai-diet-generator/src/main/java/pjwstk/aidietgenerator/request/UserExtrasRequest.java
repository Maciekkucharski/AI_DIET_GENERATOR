package pjwstk.aidietgenerator.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserExtrasRequest {
    private String background_image;
    private String about_me;
    private String profession;
}
