package pjwstk.aidietgenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjwstk.aidietgenerator.entity.DietDay;
import pjwstk.aidietgenerator.entity.DietWeek;
import pjwstk.aidietgenerator.entity.User;

@Repository
public interface WeekDietRepository extends JpaRepository<DietWeek, Long> {
    DietWeek findByuser(User user);
    DietWeek findByDaysForWeekDietContaining(DietDay dietDay);
}
