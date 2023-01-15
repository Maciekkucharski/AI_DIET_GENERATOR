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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DietWeek getDietWeek() {
        return dietWeek;
    }

    public void setDietWeek(DietWeek dietWeek) {
        this.dietWeek = dietWeek;
    }
}
