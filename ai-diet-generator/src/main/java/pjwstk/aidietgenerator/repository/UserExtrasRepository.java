package pjwstk.aidietgenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.entity.UserExtras;

@Repository
public interface UserExtrasRepository extends JpaRepository<UserExtras, Long> {
    UserExtras findByuser(User user);
}
