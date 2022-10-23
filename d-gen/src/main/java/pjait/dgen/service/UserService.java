package pjait.dgen.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import pjait.dgen.model.User;

import javax.persistence.EntityManager;

@Repository
public class UserService {
    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService (EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void saveUser(User user){
        var userEntity = new User();
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setEmail(user.getEmail());
        userEntity.setUsername (user.getUsername());
        userEntity.setPassword (passwordEncoder.encode(user.getPassword()));
        userEntity.setAuthority(user.getAuthority());
        entityManager.persist(userEntity);
    }

    public boolean doesUserExist (String username){
        if (entityManager.createQuery ("SELECT ue FROM User ue WHERE ue.username= :username", User.class)
                .setParameter ("username", username)
                .getResultList ().isEmpty ()) return false;
        else return true;
    }
    public boolean isEmpty(){
        if (entityManager.createQuery ("SELECT ue FROM User ue ", User.class)
                .getResultList ().isEmpty ()) return true;
        else return false;
    }

    public User findUserByUsername(String username){
        return entityManager.createQuery ("SELECT ue FROM User ue WHERE ue.username= :username", User.class)
                .setParameter ("username", username)
                .getSingleResult ();
    }

    public User findCurrentUser(){
        return entityManager.createQuery ("SELECT ue FROM User ue WHERE ue.username= :username", User.class)
                .setParameter ("username", getCurrentUsername())
                .getSingleResult ();
    }

    public String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof User) {
            String username = ((User) principal).getUsername();
            return username;
        }else {
            return principal.toString();
        }
    }
}