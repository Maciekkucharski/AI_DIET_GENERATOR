package pjwstk.aidietgenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.request.RegisterRequest;
import pjwstk.aidietgenerator.service.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@RestController
@RequestMapping("/account/register")
public class RegisterController {

    private UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Transactional
    public User register(@RequestBody RegisterRequest registerRequest, HttpServletResponse response) {

        if (userService.doesUserExist(registerRequest.getEmail())) {
            response.setStatus(HttpStatus.CONFLICT.value()); //User already exists.
            return null;
        } else {
            if (registerRequest.getPassword() == null || registerRequest.getPassword() == ""
            || registerRequest.getEmail() == null || registerRequest.getPassword().length() < 6) {
                response.setStatus(HttpStatus.CONFLICT.value()); //Invalid password.
                return null;
            } else {
                User newUser = new User(registerRequest.getEmail(), registerRequest.getPassword()); //New user created.
                if (userService.isEmpty()) {
                    GrantedAuthority adminAuthority = () -> "ROLE_ADMIN";
                    newUser.addAuthority(adminAuthority);
                }
                GrantedAuthority defaultAuthority = () -> "ROLE_USER";
                newUser.addAuthority(defaultAuthority);
                userService.saveUser(newUser);
                response.setStatus(HttpStatus.CREATED.value());
                return newUser;
            }
        }
    }
}