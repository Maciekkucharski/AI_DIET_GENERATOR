package pjwstk.aidietgenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "user_socials")
@NoArgsConstructor
@Setter
@Getter
public class Socials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private long id;

    @Column(name = "facebook")
    private String facebook;

    @Column(name = "twitter")
    private String twitter;

    @Column(name = "instagram")
    private String instagram;

    @Column(name = "telegram")
    private String telegram;

    @Column(name = "youtube")
    private String youtube;

    @Column(name = "discord")
    private String discord;

    public Socials(String facebook, String twitter, String instagram, String telegram, String youtube, String discord, User user) {
        this.facebook = facebook;
        this.twitter = twitter;
        this.instagram = instagram;
        this.telegram = telegram;
        this.youtube = youtube;
        this.discord = discord;
        this.user = user;
    }

    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"firstName", "lastName", "password", "authorities", "username"})
    private User user;

}
