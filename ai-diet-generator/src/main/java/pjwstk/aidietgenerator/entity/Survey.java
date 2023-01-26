package pjwstk.aidietgenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name = "surveys")
@Setter
@Getter
@NoArgsConstructor
public class Survey {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "answer1")
    @NotNull
    Integer answer1;

    @Column(name = "answer2")
    @NotNull
    Integer answer2;

    @Column(name = "answer3")
    @NotNull
    Integer answer3;

    @Column(name = "answer4")
    @NotNull
    Integer answer4;

    @Column(name = "answer5")
    @NotNull
    Integer answer5;

    @Column(name = "answer6")
    @NotNull
    Integer answer6;

    @Column(name = "answer7")
    @NotNull
    Integer answer7;

    @Column(name = "answer8")
    @NotNull
    Integer answer8;

    @Column(name = "answer9")
    @NotNull
    Integer answer9;

    @Column(name = "answer10")
    @NotNull
    Integer answer10;

    @Column(name = "answer11")
    @NotNull
    Integer answer11;

    @Column(name = "answer12")
    @NotNull
    Integer answer12;

    @Column(name = "answer13")
    @NotNull
    Integer answer13;

    @Column(name = "created_at")
    @NotNull
    private Timestamp timestamp;

    @ManyToOne
    @NotNull
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

}
