package pjwstk.aidietgenerator.entity;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "recipes_for_days")
public class DietDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @Column(name = "day_recipes")
    List<Recipe> recipesOneDay;

}
