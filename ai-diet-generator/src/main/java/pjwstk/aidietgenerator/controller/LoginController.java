package pjwstk.aidietgenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;

import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.exception.UnauthorizedException;
import pjwstk.aidietgenerator.request.LoginRequest;
import pjwstk.aidietgenerator.security.AuthenticationService;
import pjwstk.aidietgenerator.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/account")
public class LoginController {

    private OAuth2AuthorizedClientService authorizedClientService;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Autowired
    public LoginController(OAuth2AuthorizedClientService authorizedClientService,
                           AuthenticationService authenticationService,
                           UserService userService) {
        this.authorizedClientService = authorizedClientService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest loginRequest){
        var isLogged = authenticationService.login(loginRequest.getEmail(), loginRequest.getPassword());
        if(!isLogged){
            throw new UnauthorizedException();
        }
        return userService.findCurrentUser();
    }

    @GetMapping("/loginSuccess")
    public void getLoginInfo(Model model, OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName());
        String userInfoEndpointUri = client.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUri();

        if (!StringUtils.isEmpty(userInfoEndpointUri)) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken()
                    .getTokenValue());
            HttpEntity entity = new HttpEntity("", headers);
            ResponseEntity<Map> response = restTemplate
                    .exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);
            Map userAttributes = response.getBody();
            model.addAttribute("name", userAttributes.get("name"));
            model.addAttribute("email", userAttributes.get("email"));
        }
        var isLogged = authenticationService.oauth2Login(model.getAttribute("email").toString());
        if(!isLogged){
            throw new UnauthorizedException();
        }
    }
}
