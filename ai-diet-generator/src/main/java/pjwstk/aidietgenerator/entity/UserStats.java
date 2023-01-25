package pjwstk.aidietgenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

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
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getBmi() {
        return bmi;
    }

    public void setBmi(Double bmi) {
        this.bmi = bmi;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getCal() {
        return cal;
    }

    public void setCal(Integer cal) {
        this.cal = cal;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
