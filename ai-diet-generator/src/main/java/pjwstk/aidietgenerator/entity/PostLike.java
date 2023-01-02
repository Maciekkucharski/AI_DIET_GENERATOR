package pjwstk.aidietgenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class PostLike {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "created_at")
    @NotNull
    private Timestamp timestamp;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "creator_id")
    @JsonIgnoreProperties({"firstName", "lastName", "password", "authorities", "username", "email", "enabled", "authority"})
    private User user;
}
