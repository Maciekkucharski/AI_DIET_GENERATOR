package pjwstk.aidietgenerator.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@Entity
@Table(name = "user_socials")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Socials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Socials(String facebook, String twitter, String instagram, String telegram, String youtube, String discord, User user) {
        super();
        this.facebook = facebook;
        this.twitter = twitter;
        this.instagram = instagram;
        this.telegram = telegram;
        this.youtube = youtube;
        this.discord = discord;
        this.user = user;
    }

    public Socials() {

    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getDiscord() {
        return discord;
    }

    public void setDiscord(String discord) {
        this.discord = discord;
    }

    public User getUser() {
        return user;
    }
}
