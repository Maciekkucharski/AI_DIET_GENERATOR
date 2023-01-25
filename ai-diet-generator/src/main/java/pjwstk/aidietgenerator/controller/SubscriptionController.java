package pjwstk.aidietgenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import pjwstk.aidietgenerator.entity.Subscription;
import pjwstk.aidietgenerator.request.SubscriptionRequest;
import pjwstk.aidietgenerator.service.SubscriptionService;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@EnableScheduling
@RestController
@RequestMapping("/account/subscription")
@CrossOrigin(exposedHeaders = "*")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public List<Subscription> getCurrentUserSubscriptionInfo(HttpServletResponse response){
        return subscriptionService.getUserSubscription(response);
    }

    @PostMapping("/create")
    public Subscription subscribe(@RequestBody SubscriptionRequest request, HttpServletResponse response){
        return subscriptionService.createSubscription(request, response);
    }
    @GetMapping("/cancel")
    public Subscription unsubscribe(HttpServletResponse response){
        return subscriptionService.deleteSubscription(response);
    }

    @Scheduled(fixedDelay = 1000*60*60*12) // twelve hours
    public void checkSubscriptions(){
        subscriptionService.checkSubscriptionsStatus();
    }
}
