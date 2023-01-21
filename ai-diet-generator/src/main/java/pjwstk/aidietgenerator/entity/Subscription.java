package pjwstk.aidietgenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "subscriptions")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Subscription {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time")
    private Date start_time;

    @Column(name = "final_payment_time")
    private Date final_payment_time;

    @Column(name = "status")
    private String status;

    @Column(name = "valid_till")
    private Date valid_till;

    @Column(name = "payer_id")
    private String payer_id;

    @Column(name = "subscription_id")
    private String subscription_id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"firstName", "lastName", "password", "authorities", "username"})
    private User user;
}
