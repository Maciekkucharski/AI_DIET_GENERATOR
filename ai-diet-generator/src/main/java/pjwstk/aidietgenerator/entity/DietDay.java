package pjwstk.aidietgenerator.entity;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "recipes_for_day")
public class DietDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "week_id")
    private DietWeek dietWeek;

    @OneToMany
    @JoinColumn(name = "day_id")
    private List<Recipe> recipesOneDay;

}
