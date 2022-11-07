package pjwstk.aidietgenerator.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.security.Util.UserSession;
import pjwstk.aidietgenerator.service.UserService;

@Component
public class AuthenticationService {

    final UserSession userSession;
    final UserService userService;

    public AuthenticationService(UserSession userSession, UserService userService){
        this.userSession = userSession;
        this.userService = userService;
    }

    public boolean login(String username, String password){
        if(userService.doesUserExist(username)){
            final User currentUser = userService.findUserByUsername(username);
            final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if(encoder.matches(password, currentUser.getPassword())){
                userSession.logIn();
                User user = new User(currentUser.getUsername(), currentUser.getPassword(), userService.findUserByUsername(username).getAuthority());
                SecurityContextHolder.getContext().setAuthentication(new AppAuthentication(user));
                return true;
            } else {
                System.out.println("Can't match password");
                return false;
            }
        } else {
            System.out.println("User doesn't exist");
            return false;
        }
    }
}
