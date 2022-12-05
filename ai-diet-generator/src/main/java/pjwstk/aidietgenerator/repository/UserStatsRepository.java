package pjwstk.aidietgenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.entity.UserStats;

import java.util.List;

@Repository
public interface UserStatsRepository extends JpaRepository<UserStats, Long> {
    List<UserStats> findByuser(User user);
}
