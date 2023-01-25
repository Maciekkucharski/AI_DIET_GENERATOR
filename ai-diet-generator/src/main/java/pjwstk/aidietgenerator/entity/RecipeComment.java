package pjwstk.aidietgenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@Setter
@Getter
@Table(name = "recipe_comments")
public class RecipeComment {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    @NotNull
    private String content;

    @Column(name = "created_at")
    @NotNull
    private Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JsonIgnoreProperties({"password", "authorities", "username", "email", "authority"})
    private User user;

    @ManyToOne
    @JsonIgnore
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

}
