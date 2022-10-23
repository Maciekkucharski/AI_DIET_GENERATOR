package pjait.dgen.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pjait.dgen.exception.UnauthorizedException;
import pjait.dgen.security.AuthenticationService;
import pjait.dgen.service.LoginService;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthenticationService authenticationService;

    public LoginController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping()
    public void login(LoginService loginService) {
        var isLogged = authenticationService.login(loginService.getUsername(), loginService.getPassword());
        if (!isLogged) {
            throw new UnauthorizedException();
        }
    }
}
