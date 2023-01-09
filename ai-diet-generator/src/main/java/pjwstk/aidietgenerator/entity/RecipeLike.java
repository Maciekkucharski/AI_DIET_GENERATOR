package pjwstk.aidietgenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "creator_id")
    @JsonIgnoreProperties({"password", "authorities", "username", "email", "enabled", "authority",
            "credentialsNonExpired", "accountNonExpired", "accountNonLocked"})
    private User user;
}
