package pjwstk.aidietgenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.exception.ResourceNotFoundException;
import pjwstk.aidietgenerator.repository.UserRepository;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(exposedHeaders = "*")
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserController(PasswordEncoder passwordEncoder,
                          UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    // get all users
    @GetMapping("/all")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    // get user by id
    @GetMapping("/{id}")
    public User getUserById(@PathVariable (value = "id" ) long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id :" + userId));
    }

    // create user
    @PostMapping
    public User createUser(User user){
        User newUser = new User ();
        newUser.setEmail (user.getEmail ());
        newUser.setPassword (passwordEncoder.encode(user.getPassword ()));
        newUser.setCreatedAt();
        return userRepository.save(newUser);
    }

    // update user
    @PutMapping("/{id}")
    public User updateUser(User user, @PathVariable("id") long userId){
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id :" + userId));
        if(user.getFirstName () != null) existingUser.setFirstName(user.getFirstName());
        if(user.getLastName () != null) existingUser.setLastName(user.getLastName());
        if(user.getEmail () != null) existingUser.setEmail (user.getEmail ());
        if(user.getPassword () != null) existingUser.setPassword (passwordEncoder.encode(user.getPassword ()));
        return userRepository.save(existingUser);
    }

    // delete user by id
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable (value = "id" ) long userId){
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id :" + userId));
        userRepository.delete(existingUser);
        return ResponseEntity.ok().build();
    }
}
