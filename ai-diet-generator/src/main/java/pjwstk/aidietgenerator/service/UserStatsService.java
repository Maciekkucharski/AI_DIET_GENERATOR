package pjwstk.aidietgenerator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.entity.UserStats;
import pjwstk.aidietgenerator.repository.UserRepository;
import pjwstk.aidietgenerator.repository.UserStatsRepository;
import pjwstk.aidietgenerator.request.ImagePathRequest;

import javax.servlet.http.HttpServletResponse;

@Service
public class UserStatsService {

    private final UserDetailsService userDetailsService;
    private final UserStatsRepository userStatsRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserStatsService(UserDetailsService userDetailsService,
                            UserStatsRepository userStatsRepository,
                            UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.userStatsRepository = userStatsRepository;
        this.userRepository = userRepository;
    }

    public double calculateBmi(double weight, int height){
        double dHeight = height;
        double heightInMeters = dHeight/100;

        return (weight/Math.pow(heightInMeters, 2));
    }

    public UserStats saveUserStats(UserStats userStats){
        var userStatsEntity = new UserStats();
        userStatsEntity.setWeight(userStats.getWeight());
        userStatsEntity.setHeight(userStats.getHeight());
        userStatsEntity.setAge(userStats.getAge());
        userStatsEntity.setBmi(calculateBmi(userStats.getWeight(), userStats.getHeight()));
        userStatsEntity.setGender(userStats.getGender());
        userStatsEntity.setUpdatedAt();
        userStatsEntity.setUser(userDetailsService.findCurrentUser());
        return userStatsRepository.save(userStatsEntity);
    }


    public void setUserImage(ImagePathRequest request, HttpServletResponse response) {
        User currentUser = userDetailsService.findCurrentUser();
        if(currentUser != null){
            if(request.getImagePath() !=null || !request.getImagePath().equals("")) {
                currentUser.setImagePath(request.getImagePath());
                response.setStatus(HttpStatus.OK.value());
                userRepository.save(currentUser);
            } else {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
            }
        } else {
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }
    }
}
