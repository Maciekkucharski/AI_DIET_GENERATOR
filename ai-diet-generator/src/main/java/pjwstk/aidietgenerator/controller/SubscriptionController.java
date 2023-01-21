package pjwstk.aidietgenerator.controller;

import lombok.Getter;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import pjwstk.aidietgenerator.entity.Subscription;
import pjwstk.aidietgenerator.request.SubscriptionRequest;
import pjwstk.aidietgenerator.service.SubscriptionService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableScheduling
@RestController
@RequestMapping("/account/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public Subscription getCurrentUserSubscriptionInfo(HttpServletResponse response){
        return subscriptionService.getUserSubscription(response);
    }

    @PostMapping("/create")
    public Subscription subscribe(@RequestBody SubscriptionRequest request, HttpServletResponse response){
        return subscriptionService.createSubscription(request, response);
    }
    @PostMapping("/cancel")
    public Subscription unsubscribe(@RequestBody SubscriptionRequest request, HttpServletResponse response) throws IOException {
        return subscriptionService.deleteSubscription(request, response);
    }

    @Scheduled(fixedDelay = 1000*60*60) // one hour
    public void checkSubscriptions(){
        subscriptionService.checkSubscriptionsStatus();
    }
}
