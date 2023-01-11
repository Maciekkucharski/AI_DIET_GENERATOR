package pjwstk.aidietgenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjwstk.aidietgenerator.entity.DietDay;

@Repository
public interface DayDietRepository extends JpaRepository<DietDay, Long> {
}
