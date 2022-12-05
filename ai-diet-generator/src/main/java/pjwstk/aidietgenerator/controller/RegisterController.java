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
    public void register(@RequestBody RegisterRequest registerRequest, HttpServletResponse response) {

        if (userDetailsService.doesUserExist(registerRequest.getEmail())) {
            response.setStatus(HttpStatus.CONFLICT.value()); //User already exists.
        } else {
            if (registerRequest.getPassword() == null || registerRequest.getPassword() == ""
                    || registerRequest.getEmail() == null || registerRequest.getPassword().length() < 6) {
                response.setStatus(HttpStatus.CONFLICT.value()); //Invalid password.
            } else {
                User newUser = new User(registerRequest.getEmail(), registerRequest.getPassword()); //New user created.
                if (userDetailsService.isEmpty()) {
                    GrantedAuthority adminAuthority = () -> "ROLE_ADMIN";
                    newUser.addAuthority(adminAuthority);
                }
                GrantedAuthority defaultAuthority = () -> "ROLE_USER";
                newUser.addAuthority(defaultAuthority);
                userDetailsService.saveUser(newUser);
                response.setStatus(HttpStatus.CREATED.value());
            }
        }
    }
}