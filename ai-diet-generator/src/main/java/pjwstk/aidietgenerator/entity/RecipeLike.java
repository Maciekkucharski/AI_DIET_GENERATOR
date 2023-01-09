package pjwstk.aidietgenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "recipe_likes")
public class RecipeLike {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "created_at")
    @NotNull
    private Timestamp timestamp;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "creator_id")
    @JsonIgnoreProperties({"firstName", "lastName", "password", "authorities", "username", "email", "enabled", "authority"})
    private User user;
}
