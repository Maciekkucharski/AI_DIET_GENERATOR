package pjwstk.aidietgenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "following")
@NoArgsConstructor
@Setter
@Getter
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"firstName", "lastName", "password", "authorities", "username", "authority", "email" ,"subscribed"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    @JsonIgnoreProperties({"firstName", "lastName", "password", "authorities", "username", "authority", "email","subscribed"})
    private User follower;

}
