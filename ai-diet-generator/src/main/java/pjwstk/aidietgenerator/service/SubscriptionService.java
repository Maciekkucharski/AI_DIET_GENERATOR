package pjwstk.aidietgenerator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pjwstk.aidietgenerator.entity.Subscription;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.repository.SubscriptionRepository;
import pjwstk.aidietgenerator.repository.UserRepository;
import pjwstk.aidietgenerator.request.SubscriptionRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@EnableScheduling
@Service
public class SubscriptionService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SubscriptionService(UserRepository userRepository,
                               SubscriptionRepository subscriptionRepository,
                               UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.userDetailsService = userDetailsService;
    }

    public Subscription createSubscription(SubscriptionRequest request, HttpServletResponse response) {
        User currentUser = userDetailsService.findCurrentUser();
        if (currentUser != null) {
            Subscription existingSubscription = subscriptionRepository.findByUserAndStatus(currentUser, "ACTIVE");
            if (request.getEmail_address() != null && request.getStatus().equals("ACTIVE") && request.getStart_time() != null
                    && request.getFinal_payment_time() != null && request.getPayer_id() != null & request.getId() != null
                    && existingSubscription == null) {
                Subscription newSubscription = new Subscription();
                newSubscription.setUser(currentUser);
                newSubscription.setStatus(request.getStatus());
                newSubscription.setStart_time(request.getStart_time());
                newSubscription.setFinal_payment_time(request.getFinal_payment_time());
                newSubscription.setPayer_id(request.getPayer_id());
                newSubscription.setSubscription_id(request.getId());
                newSubscription.setValid_till(new Date(request.getStart_time().getTime() + (1000L * 60 * 60 * 24 * 30))); // + 30 days
                currentUser.setSubscribed(true);
                userRepository.save(currentUser);
                response.setStatus(HttpStatus.CREATED.value());
                return subscriptionRepository.save(newSubscription);
            } else {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return null;
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        }
    }

    public Subscription deleteSubscription(HttpServletResponse response) {
        User currentUser = userDetailsService.findCurrentUser();
        if (currentUser != null) {
            Subscription userSubscription = subscriptionRepository.findByUserAndStatus(currentUser, "ACTIVE");
            if (userSubscription != null) {
                String urlString = "https://api-m.sandbox.paypal.com/v1/billing/subscriptions/" + userSubscription.getSubscription_id() + "/cancel";
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                String plainCreds = "ARmk2ZR1YkM6uHxsqnE4IC3DqaSazz7MFO2lJsA3eKl4vFbgDqBsbpHZefq2FWzkGskDq77CVrrebypy:EIWIQsYghcbsm_EwAEV_BuYJCQN_D6slPIvkf_T38zQ7lzOga2KQ5hqbEd_ZUCgQ1p6lygFeZfQaTiAX";
                byte[] plainCredsBytes = plainCreds.getBytes();
                byte[] base64CredsBytes = Base64.getEncoder().encode(plainCredsBytes);
                String base64Creds = new String(base64CredsBytes);
                headers.add("Authorization", "Basic " + base64Creds);
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlString)
                        .queryParam("Reason", "Not satisfied with the service");
                HttpEntity<?> entity = new HttpEntity<>(headers);
                restTemplate.exchange(
                        builder.toUriString(),
                        HttpMethod.POST,
                        entity,
                        String.class);

                userSubscription.setStatus("CANCELED");
                response.setStatus(HttpStatus.OK.value());
                return subscriptionRepository.save(userSubscription);
            } else {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return null;
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        }
    }

    public List<Subscription> getUserSubscription(HttpServletResponse response) {
        User currentUser = userDetailsService.findCurrentUser();
        if (currentUser != null) {
            List<Subscription> userSubscription = subscriptionRepository.findByUser(currentUser);
            if (userSubscription != null) {
                response.setStatus(HttpStatus.OK.value());
                return userSubscription;
            } else {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return null;
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        }
    }

    public void checkSubscriptionsStatus() {
        //https://developer.paypal.com/docs/api/subscriptions/v1/#subscriptions_patch sprawdzanie statusu
        List<Subscription> allSubscriptions = subscriptionRepository.findAll();
        Date currentDate = new Date();
        for (Subscription subscription : allSubscriptions) {
            if (currentDate.after(subscription.getValid_till())) {
                if (Objects.equals(subscription.getStatus(), "ACTIVE") &&
                        currentDate.before(new Date(subscription.getFinal_payment_time().getTime() + (1000L * 60 * 60 * 24 * 30)))) {
                    subscription.setValid_till(new Date(subscription.getValid_till().getTime() + (1000L * 60 * 60 * 24 * 30))); // plus 30 days
                    subscriptionRepository.save(subscription);
                } else if (Objects.equals(subscription.getStatus(), "CANCELED")) {
                    subscription.getUser().setSubscribed(false);
                    userRepository.save(subscription.getUser());
                } else if (currentDate.after(new Date(subscription.getFinal_payment_time().getTime() + (1000L * 60 * 60 * 24 * 30)))) {
                    subscription.setStatus("CANCELED");
                    User user = subscription.getUser();
                    user.setSubscribed(false);
                    subscriptionRepository.save(subscription);
                    userRepository.save(user);
                }
            }
        }
    }
}
