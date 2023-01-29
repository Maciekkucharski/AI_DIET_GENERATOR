package pjwstk.aidietgenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjwstk.aidietgenerator.entity.Rating;
import pjwstk.aidietgenerator.entity.Recipe;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByrecipe(Recipe recipe);
}
