package pjwstk.aidietgenerator.service;


import org.springframework.beans.factory.annotation.Autowired;
import pjwstk.aidietgenerator.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;


@Repository
public class UserService {

    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService (EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void saveUser(User user){
        var userEntity = new User();
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword (passwordEncoder.encode(user.getPassword()));
        userEntity.setAuthority(String.join(",", user.getAuthority()));
        userEntity.setCreatedAt();
        entityManager.persist(userEntity);
    }

    public boolean doesUserExist (String email){
        if (entityManager.createQuery ("SELECT ue FROM User ue WHERE ue.email= :email", User.class)
                .setParameter ("email", email)
                .getResultList ().isEmpty ()) return false;
        else return true;
    }
    public boolean isEmpty(){
        if (entityManager.createQuery ("SELECT ue FROM User ue ", User.class)
                .getResultList ().isEmpty ()) return true;
        else return false;
    }

    public User findUserByUsername(String email){
            return entityManager.createQuery ("SELECT ue FROM User ue WHERE ue.email= :email", User.class)
                .setParameter ("email", email)
                .getSingleResult ();
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
            currentUser = entityManager.createQuery("SELECT user FROM User user WHERE user.email= :email", User.class)
                    .setParameter("email", getCurrentUserEmail())
                    .getSingleResult();
        } catch (NoResultException e) {
            System.out.println(e.getMessage());
        }
        return currentUser;
    }
}
