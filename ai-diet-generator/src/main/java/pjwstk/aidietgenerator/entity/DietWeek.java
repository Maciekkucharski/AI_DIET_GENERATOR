package pjwstk.aidietgenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipes_for_week")
public class DietWeek {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST})
    private List<DietDay> daysForWeekDiet;

    @OneToOne
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"firstName", "lastName", "password", "authorities", "email"})
    private User user;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    public DietWeek(){}

    public DietWeek(List<DietDay> daysForWeekDiet, Timestamp timestamp, User user){
        this.daysForWeekDiet = daysForWeekDiet;
        this.timestamp = timestamp;
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

    public List<Long> getRecipeIdsForTheWeek(){
        List<Long> thisWeeksRecipesIds = new ArrayList<>();
        for(DietDay oneDay : daysForWeekDiet){
            List<Recipe> oneDaysRecipes = oneDay.getRecipesForToday();
            for(Recipe recipe : oneDaysRecipes){
                if(!thisWeeksRecipesIds.contains(recipe.getId())) {
                    thisWeeksRecipesIds.add(recipe.getId());
                }
            }
        }
        return thisWeeksRecipesIds;
    }

    public void setCreatedAt(){
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
