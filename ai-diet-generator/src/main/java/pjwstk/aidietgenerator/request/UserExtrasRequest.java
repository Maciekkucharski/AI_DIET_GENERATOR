package pjwstk.aidietgenerator.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserExtrasRequest {
    private String backgroundImagePath;
    private String about_me;
    private String profession;
    private String facebook;
    private String twitter;
    private String instagram;
    private String telegram;
    private String youtube;
    private String discord;
}
