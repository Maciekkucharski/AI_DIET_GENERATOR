package pjait.dgen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;

import pjait.dgen.model.User;
import pjait.dgen.service.RegisterService;
import pjait.dgen.service.UserService;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserService userService;


    @PostMapping
    @Transactional
    public void register(RegisterService registerService, HttpServletResponse response){

        if(userService.doesUserExist (registerService.getUsername ())){
            response.setStatus(HttpStatus.CONFLICT.value ()); //User already exists.
        }else {
            if(registerService.getPassword ()==null || registerService.getPassword ()==""){
                response.setStatus (HttpStatus.CONFLICT.value ()); //Invalid password.
            }else {
                User newUser = new User(registerService.getFirst_name(), registerService.getLast_name(), registerService.getEmail() ,registerService.getUsername(),registerService.getPassword()); //New user created.
                if(userService.isEmpty ()){
                    GrantedAuthority adminAuthority = () -> "ROLE_ADMIN";
                    newUser.addAuthority(adminAuthority);
                }
                GrantedAuthority defaultAuthority = () -> "ROLE_USER";
                newUser.addAuthority(defaultAuthority);
                userService.saveUser(newUser);
            }
        }
    }
}