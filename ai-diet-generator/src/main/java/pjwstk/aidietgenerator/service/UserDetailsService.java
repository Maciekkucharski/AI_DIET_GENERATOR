package pjwstk.aidietgenerator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.entity.UserDetails;

import javax.persistence.EntityManager;

@Service
public class UserDetailsService {

    private final EntityManager entityManager;

    @Autowired
    UserService userService;

    public UserDetailsService(EntityManager entityManager) { this.entityManager = entityManager;}

    public double calculateBmi(double weight, int height){
        double dHeight = height;
        double heightInMeters = dHeight/100;

        return (weight/Math.pow(heightInMeters, 2));
    }

    public void saveUserDetails(UserDetails userDetails){
        var userDetailsEntity = new UserDetails();
        userDetailsEntity.setWeight(userDetails.getWeight());
        userDetailsEntity.setHeight(userDetails.getHeight());
        userDetailsEntity.setAge(userDetails.getAge());
        userDetailsEntity.setBmi(calculateBmi(userDetails.getWeight(), userDetails.getHeight()));
        userDetailsEntity.setGender(userDetails.getGender());
        userDetailsEntity.setUpdatedAt();
        userDetailsEntity.setUser(userService.findCurrentUser());
        entityManager.persist(userDetailsEntity);
    }





}
