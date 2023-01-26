package pjwstk.aidietgenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pjwstk.aidietgenerator.entity.Gender;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.entity.UserStats;
import pjwstk.aidietgenerator.exception.ResourceNotFoundException;
import pjwstk.aidietgenerator.repository.UserStatsRepository;
import pjwstk.aidietgenerator.request.ImagePathRequest;
import pjwstk.aidietgenerator.request.UserStatsRequest;
import pjwstk.aidietgenerator.service.UserDetailsService;
import pjwstk.aidietgenerator.service.UserStatsService;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/account/stats")
@CrossOrigin(exposedHeaders = "*")
public class UserStatsController {
    private final UserStatsRepository userStatsRepository;
    private final UserDetailsService userDetailsService;
    private final UserStatsService userStatsService;

    @Autowired
    public UserStatsController(UserStatsRepository userStatsRepository,
                               UserDetailsService userDetailsService,
                               UserStatsService userStatsService) {
        this.userStatsRepository = userStatsRepository;
        this.userDetailsService = userDetailsService;
        this.userStatsService = userStatsService;
    }

    @GetMapping
    public List<UserStats> getCurrentUserStats() {
        return userStatsRepository.findByuser(userDetailsService.findCurrentUser());
    }

    @PostMapping
    @Transactional
    public UserStats addUserDetails(@RequestBody UserStatsRequest userStats, HttpServletResponse response) {

        User currentUser = userDetailsService.findCurrentUser();
        if (currentUser == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        } else {
            UserStats newUserStats = new UserStats();
            if(userStats.getWeight() != 0){
                newUserStats.setWeight(userStats.getWeight());
            }
            if(userStats.getHeight() != 0){
                newUserStats.setHeight(userStats.getHeight());
            }
            if(userStats.getAge() != 0){
                newUserStats.setAge(userStats.getAge());
            }
            if(userStats.getGender() != null){
                newUserStats.setGender(userStats.getGender() == "male" ? Gender.MALE : Gender.FEMALE);
            }
            userStatsService.saveUserStats(newUserStats);
            response.setStatus(HttpStatus.CREATED.value());
            return newUserStats;
        }
    }

    @GetMapping("/{id}")
    public UserStats getUserStatsById(@PathVariable (value = "id") long userDetailsId) {
        return userStatsRepository.findById(userDetailsId).
                orElseThrow(() -> new ResourceNotFoundException("UserDetails not found with id :" + userDetailsId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserStats> deleteUserStats(@PathVariable (value = "id") long userDetailsId) {
        UserStats existingUserStats = userStatsRepository.findById(userDetailsId).
                orElseThrow(() -> new ResourceNotFoundException("UserDetails not found with id :" + userDetailsId));
        userStatsRepository.delete(existingUserStats);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/image")
    public void setUserImage(@RequestBody ImagePathRequest request, HttpServletResponse response){
        userStatsService.setUserImage(request, response);
    }
}
