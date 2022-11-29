package pjwstk.aidietgenerator.security;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public AuthenticationService(UserSession userSession, UserService userService){
        this.userSession = userSession;
        this.userService = userService;
    }

    public boolean login(String email, String password){
        if(userService.doesUserExist(email)){
            final User currentUser = userService.findByEmail(email);
            final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if(encoder.matches(password, currentUser.getPassword())){
                userSession.logIn();
                User user = new User(currentUser.getEmail(), currentUser.getPassword(), userService.findByEmail(email).getAuthority());
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

    public boolean oauth2Login(String email){
        if(userService.doesUserExist(email)){
            final User currentUser = userService.findByEmail(email);
            userSession.logIn();
            User user = new User(currentUser.getEmail(), currentUser.getPassword(), userService.findByEmail(email).getAuthority());
            SecurityContextHolder.getContext().setAuthentication(new AppAuthentication(user));
            return true;
        } else {
            System.out.println("User doesn't exist");
            return false;
        }
    }
}
