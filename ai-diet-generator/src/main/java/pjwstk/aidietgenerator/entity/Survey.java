package pjwstk.aidietgenerator.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "surveys")
@Setter
@Getter
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "exclusions")
    String exclusions;

    @Column(name = "answer1")
    Integer answer1;

    @Column(name = "answer2")
    Integer answer2;

    @Column(name = "answe3")
    Integer answer3;

    @Column(name = "answer4")
    Integer answer4;

    @Column(name = "answer5")
    Integer answer5;

    @Column(name = "answer6")
    Integer answer6;

    @Column(name = "answer7")
    Integer answer7;

    @Column(name = "answer8")
    Integer answer8;

    @Column(name = "answer9")
    Integer answer9;

    @Column(name = "answer10")
    Integer answer10;

    @Column(name = "answer11")
    Integer answer11;

    @Column(name = "answer12")
    Integer answer12;

    @Column(name = "answer13")
    Integer answer13;

    @Column(name = "created_at")
    private Timestamp timestamp;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_id")
    private User user;

    public Survey(String exclusions, Integer answer1, Integer answer2, Integer answer3, Integer answer4, Integer answer5, Integer answer6, Integer answer7, Integer answer8, Integer answer9, Integer answer10, Integer answer11, Integer answer12, Integer answer13) {
        super();
        this.exclusions = exclusions;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.answer5 = answer5;
        this.answer6 = answer6;
        this.answer7 = answer7;
        this.answer8 = answer8;
        this.answer9 = answer9;
        this.answer10 = answer10;
        this.answer11 = answer11;
        this.answer12 = answer12;
        this.answer13 = answer13;
    }

    public Survey() {
    }
}
