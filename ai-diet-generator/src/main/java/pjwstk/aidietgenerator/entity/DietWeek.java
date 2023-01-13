package pjwstk.aidietgenerator.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "recipes_for_weeks")
public class DietWeek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @Column(name = "week_recipes")
    private List<DietDay> weekRecipesList;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
