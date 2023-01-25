package pjwstk.aidietgenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_extras")
public class UserExtras {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "background_image")
    private String backgroundImagePath;

    @Column(name = "about_me")
    private String about_me;

    @Column(name = "profession")
    private String profession;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "creator_id")
    @JsonIgnore
    private User user;
}
