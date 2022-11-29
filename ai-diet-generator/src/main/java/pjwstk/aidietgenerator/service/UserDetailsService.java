package pjwstk.aidietgenerator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.entity.UserDetails;
import pjwstk.aidietgenerator.repository.UserDetailsRepository;

@Service
public class UserDetailsService {

    private final UserService userService;
    private final UserDetailsRepository userDetailsRepository;

    @Autowired
    public UserDetailsService(UserService userService, UserDetailsRepository userDetailsRepository) {
        this.userService = userService;
        this.userDetailsRepository = userDetailsRepository;
    }

    public double calculateBmi(double weight, int height){
        double dHeight = height;
        double heightInMeters = dHeight/100;

        return (weight/Math.pow(heightInMeters, 2));
    }

    public UserDetails saveUserDetails(UserDetails userDetails){
        var userDetailsEntity = new UserDetails();
        userDetailsEntity.setWeight(userDetails.getWeight());
        userDetailsEntity.setHeight(userDetails.getHeight());
        userDetailsEntity.setAge(userDetails.getAge());
        userDetailsEntity.setBmi(calculateBmi(userDetails.getWeight(), userDetails.getHeight()));
        userDetailsEntity.setGender(userDetails.getGender());
        userDetailsEntity.setUpdatedAt();
        userDetailsEntity.setUser(userService.findCurrentUser());
        return userDetailsRepository.save(userDetailsEntity);
    }


}
