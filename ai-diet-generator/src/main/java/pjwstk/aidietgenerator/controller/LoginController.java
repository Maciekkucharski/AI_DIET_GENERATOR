package pjwstk.aidietgenerator.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.exception.UnauthorizedException;
import pjwstk.aidietgenerator.request.LoginRequest;
import pjwstk.aidietgenerator.security.AuthenticationService;
import pjwstk.aidietgenerator.service.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@RestController
@RequestMapping("/account/login")
public class LoginController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    public LoginController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping
    public User login(@RequestBody LoginRequest loginRequest) {
        var isLogged = authenticationService.login(loginRequest.getEmail(), loginRequest.getPassword());
        if (!isLogged) {
            throw new UnauthorizedException();
        }
        return userService.findCurrentUser();
    }
}
