package pjwstk.aidietgenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.entity.UserDetails;
import pjwstk.aidietgenerator.exception.ResourceNotFoundException;
import pjwstk.aidietgenerator.repository.UserDetailsRepository;
import pjwstk.aidietgenerator.service.UserDetailsService;
import pjwstk.aidietgenerator.service.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/userDetails")
public class UserDetailsController {

    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping
    public List<UserDetails> getCurrentUserDetails() {
        return this.userDetailsRepository.findByuser(userService.findCurrentUser());
    }

    @PostMapping
    @Transactional
    public void addUserDetails(@RequestBody UserDetails userDetails, HttpServletResponse response) {
        User currentUser = userService.findCurrentUser();
        if (currentUser == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            UserDetails newUserDetails = new UserDetails();
            if(userDetails.getWeight() != 0){
                newUserDetails.setWeight(userDetails.getWeight());
            }
            if(userDetails.getHeight() != 0){
                newUserDetails.setHeight(userDetails.getHeight());
            }
            if(userDetails.getAge() != 0){
                newUserDetails.setAge(userDetails.getAge());
            }
            if(userDetails.getGender() != null){
                newUserDetails.setGender(userDetails.getGender());
            }
            response.setStatus(HttpStatus.CREATED.value());
            userDetailsService.saveUserDetails(newUserDetails);
        }
    }

    @GetMapping("/{id}")
    public UserDetails getUserDetailsById(@PathVariable (value = "id") long userDetailsId) {
        return this.userDetailsRepository.findById(userDetailsId).
                orElseThrow(() -> new ResourceNotFoundException("UserDetails not found with id :" + userDetailsId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDetails> deleteUserDetails(@PathVariable (value = "id") long userDetailsId) {
        UserDetails existingUserDetails = userDetailsRepository.findById(userDetailsId).
                orElseThrow(() -> new ResourceNotFoundException("UserDetails not found with id :" + userDetailsId));
        this.userDetailsRepository.delete(existingUserDetails);
        return ResponseEntity.ok().build();
    }
}
