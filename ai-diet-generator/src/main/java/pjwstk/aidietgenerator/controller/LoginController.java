package pjwstk.aidietgenerator.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pjwstk.aidietgenerator.exception.UnauthorizedException;
import pjwstk.aidietgenerator.request.LoginRequest;
import pjwstk.aidietgenerator.security.AuthenticationService;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@RestController
@RequestMapping("/account/login")
public class LoginController {

    private final AuthenticationService authenticationService;

    public LoginController(AuthenticationService authenticationService){
        this.authenticationService=authenticationService;
    }

    @PostMapping
    public void login(@RequestBody LoginRequest loginRequest){
        var isLogged = authenticationService.login(loginRequest.getEmail(), loginRequest.getPassword());
        if(!isLogged){
            throw new UnauthorizedException();
        }
    }
}
