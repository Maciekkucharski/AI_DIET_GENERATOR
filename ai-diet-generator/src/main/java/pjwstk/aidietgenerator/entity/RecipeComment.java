package pjwstk.aidietgenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @JsonIgnoreProperties({"firstName", "lastName", "password", "authorities", "username"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

}
