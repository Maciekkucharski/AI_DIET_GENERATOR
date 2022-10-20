package pjait.dgen.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import pjait.dgen.model.User;
import pjait.dgen.service.UserService;
import pjait.dgen.utils.UserSession;

@Component
public class AuthenticationService {

    final
    UserSession userSession;
    final
    UserService userService;

    public AuthenticationService (UserSession userSession, UserService userService) {
        this.userSession = userSession;
        this.userService = userService;
    }

    public boolean login(String username, String password){
        if(userService.doesUserExist (username)){
            final User currentUser = userService.findUserByUsername (username);
            final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder ();
            if(encoder.matches(password,currentUser.getPassword ())){
                userSession.logIn();
                User user = new User (currentUser.getUsername(), currentUser.getPassword(), userService.findUserByUsername(username).getAuthority());
                SecurityContextHolder.getContext().setAuthentication(new AppAuthentication (user));
                return true; // Logged in.
            }else{
                return false; // Invalid password.
            }
        }else{
            return false; // User not found.
        }
    }
}