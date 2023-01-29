package pjwstk.aidietgenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjwstk.aidietgenerator.entity.DietDay;
import pjwstk.aidietgenerator.entity.DietWeek;
import pjwstk.aidietgenerator.entity.Recipe;
import pjwstk.aidietgenerator.entity.User;

import java.util.List;

@Repository
public interface DayDietRepository extends JpaRepository<DietDay, Long> {
    List<DietDay> findByRecipesForTodayContaining(Recipe recipe);
}
