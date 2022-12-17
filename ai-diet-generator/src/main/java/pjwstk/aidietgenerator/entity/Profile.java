package pjwstk.aidietgenerator.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Profile {

    private User user;
    private List<UserStats> userStats;
    private Socials socials;
    private String profilePicturePath;
}
