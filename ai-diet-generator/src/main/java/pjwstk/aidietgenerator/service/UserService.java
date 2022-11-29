package pjwstk.aidietgenerator.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.repository.UserRepository;
import javax.persistence.NoResultException;



@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user){
        var userEntity = new User();
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword (passwordEncoder.encode(user.getPassword()));
        userEntity.setAuthority(String.join(",", user.getAuthority()));
        userEntity.setCreatedAt();
        userRepository.save(userEntity);
    }

    public boolean doesUserExist (String email){
        if (userRepository.findByemail(email) == null) return false;
        else return true;
    }
    public boolean isEmpty(){
        if (userRepository.findAll().isEmpty()) return true;
        else return false;
    }

    public User findByEmail(String email){
        return userRepository.findByemail(email);
    }

    public String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            String email = ((User) principal).getEmail();
            return email;
        } else {
            return principal.toString();
        }
    }

    public User findCurrentUser() {
        User currentUser = null;
        try {
            currentUser = userRepository.findByemail(getCurrentUserEmail());
        } catch (NoResultException e) {
            System.out.println(e.getMessage());
        }
        return currentUser;
    }
}
