package pjwstk.aidietgenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="user_stats")
@NoArgsConstructor
@Setter
@Getter
public class UserStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "height")
    private Integer height;

    @Column(name = "age")
    private Integer age;

    @Column(name = "bmi")
    private Double bmi;

    @Column(name = "gender")
    private Gender gender;

    @Column(name = "cal")
    private Integer cal;

    @Column(name = "updated_at")
    private Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"firstName", "lastName", "password", "authorities", "username"})
    private User user;

    public UserStats(double weight, int height, int age, double bmi, int cal, Gender gender) {
        super();
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.bmi = bmi;
        this.cal = cal;
        this.gender = gender;
    }

    public UserStats(double weight, int height, int age, Gender gender) {
        super();
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.gender = gender;
    }

    public void setUpdatedAt() {
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }
}
