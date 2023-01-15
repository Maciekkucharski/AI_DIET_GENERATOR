package pjwstk.aidietgenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "recipes_for_week")
public class DietWeek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<DietDay> daysForWeekDiet;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"firstName", "lastName", "password", "authorities", "email"})
    private User user;

    public DietWeek(){}

    public DietWeek(List<DietDay> daysForWeekDiet, User user){
        this.daysForWeekDiet = daysForWeekDiet;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<DietDay> getDaysForWeekDiet() {
        return daysForWeekDiet;
    }

    public void setDaysForWeekDiet(List<DietDay> daysForWeekDiet) {
        this.daysForWeekDiet = daysForWeekDiet;
    }
}
