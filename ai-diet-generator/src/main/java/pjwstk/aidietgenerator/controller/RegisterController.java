package pjwstk.aidietgenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.repository.UserRepository;
import pjwstk.aidietgenerator.request.RegisterRequest;
import pjwstk.aidietgenerator.service.UserDetailsService;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@RestController
@RequestMapping("/account")
@CrossOrigin(exposedHeaders = "*")
public class RegisterController {

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @Autowired
    public RegisterController(UserDetailsService userDetailsService,
                              UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public User getLoggedUser(HttpServletResponse response) {
        User loggedUser = userDetailsService.findCurrentUser();
        if(loggedUser == null){
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        } else {
            response.setStatus(HttpStatus.OK.value());
            return loggedUser;
        }
    }

    @PostMapping("/register")
    @Transactional
    public User register(@RequestBody RegisterRequest registerRequest, HttpServletResponse response) {

        if (userDetailsService.doesUserExist(registerRequest.getEmail())) {
            response.setStatus(HttpStatus.CONFLICT.value()); //User already exists.
            return null;
        } else {
            if (registerRequest.getPassword() == null || registerRequest.getPassword() == ""
                    || registerRequest.getEmail() == null || registerRequest.getPassword().length() < 6
                    || !userDetailsService.patternMatches(registerRequest.getEmail())) {
                response.setStatus(HttpStatus.CONFLICT.value()); //Invalid password.
                return null;
            } else {
                User newUser = new User(registerRequest.getEmail(), registerRequest.getPassword()); //New user created.
                if (userDetailsService.isEmpty()) {
                    GrantedAuthority adminAuthority = () -> "ROLE_ADMIN";
                    newUser.addAuthority(adminAuthority);
                }
                GrantedAuthority defaultAuthority = () -> "ROLE_USER";
                newUser.addAuthority(defaultAuthority);
                User savedUser = userDetailsService.saveUser(newUser);
                savedUser.setLastName(savedUser.getId().toString());
                savedUser.setFirstName("User");
                savedUser.setSubscribed(false);
                savedUser.setRating(false);
                savedUser.setSurvey(false);
                response.setStatus(HttpStatus.CREATED.value());
                return userRepository.save(savedUser);
            }
        }
    }
}