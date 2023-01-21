package pjwstk.aidietgenerator.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SubscriptionRequest {
    private Date start_time;
    private Date final_payment_time;
    private String email_address;
    private String status;
    private String payer_id;
    private String id;
}
