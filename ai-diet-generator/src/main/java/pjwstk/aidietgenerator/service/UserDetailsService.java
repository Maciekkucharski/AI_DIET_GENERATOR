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
import java.util.regex.Pattern;


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

    public User saveUser(User user){
        var userEntity = new User();
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword (passwordEncoder.encode(user.getPassword()));
        userEntity.setAuthority(String.join(",", user.getAuthority()));
        userEntity.setCreatedAt();
        return userRepository.save(userEntity);
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

    public User findCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return ((User) principal);
        } else {
            return null;
        }
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

    public boolean patternMatches(String email) {
        return Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
                .matcher(email)
                .matches();
    }
}
