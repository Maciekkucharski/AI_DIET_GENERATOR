package pjwstk.aidietgenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjwstk.aidietgenerator.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByemail(String email);
    List<User> findByFirstNameOrLastName(String firstName, String lastName);
    List<User> findByFirstNameAndLastName(String firstName, String lastName);
}
