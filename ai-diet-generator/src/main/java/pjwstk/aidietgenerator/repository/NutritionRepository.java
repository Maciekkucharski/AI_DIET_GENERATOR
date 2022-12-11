package pjwstk.aidietgenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjwstk.aidietgenerator.entity.Nutrition;
import pjwstk.aidietgenerator.entity.Recipe;

@Repository
public interface NutritionRepository extends JpaRepository<Nutrition, Long> {
    Nutrition findByrecipe(Recipe recipe);
}
