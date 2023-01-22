package pjwstk.aidietgenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pjwstk.aidietgenerator.entity.Follow;
import pjwstk.aidietgenerator.entity.User;
import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByUser(User user);
    Follow findByUserAndFollower(User user, User follower);
    List<Follow> findByFollower(User follower);
}
