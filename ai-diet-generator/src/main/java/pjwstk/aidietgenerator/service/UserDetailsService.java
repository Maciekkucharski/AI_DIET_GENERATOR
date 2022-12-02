package pjwstk.aidietgenerator.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.repository.UserRepository;
import javax.persistence.NoResultException;
import java.util.ArrayList;


@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailsService(UserRepository userRepository,
                              PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByemail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                new ArrayList<>());
    }
}
