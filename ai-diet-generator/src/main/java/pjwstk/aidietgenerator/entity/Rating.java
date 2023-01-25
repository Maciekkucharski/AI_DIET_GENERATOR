package pjwstk.aidietgenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@Table(name = "ratings")
@NoArgsConstructor
@Getter
@Setter
public class Rating {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "score")
    private Integer score;


    @ManyToOne
    @JoinColumn(name = "recipe_id")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JsonIgnoreProperties({"saltiness",
            "sourness",
            "sweetness",
            "bitterness",
            "spiciness",
            "fattiness",
            "servings",
            "readyInMinutes",
            "imagePath",
            "instructions",
            "vegetarian",
            "vegan",
            "glutenFree",
            "dairyFree",
            "veryHealthy",
            "verified",
            "timestamp",
            "calories",
            "carbs",
            "fat",
            "protein",
            "recipesIngredients"})
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JsonIgnoreProperties({"username", "email", "authority","authorities", "subscribed"})
    private User user;
}
