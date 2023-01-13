package pjwstk.aidietgenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pjwstk.aidietgenerator.entity.Survey;
import pjwstk.aidietgenerator.entity.User;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    Survey findByuser(User user);
}
