package pjwstk.aidietgenerator.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@Table(name = "ingredients")
@NoArgsConstructor
@Setter
@Getter
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "unit")
    private String unit;

    @ManyToOne
    @JsonIgnore
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    public Ingredient(String name, Double amount, String unit, Recipe recipe) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
        this.recipe = recipe;
    }
}
