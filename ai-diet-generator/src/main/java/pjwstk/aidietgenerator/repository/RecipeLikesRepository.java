package pjwstk.aidietgenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjwstk.aidietgenerator.entity.*;

import java.util.List;

@Repository
public interface RecipeLikesRepository extends JpaRepository<RecipeLike, Long> {
    List<RecipeLike> findByrecipe(Recipe recipe);
    RecipeLike findByUserAndRecipe(User user, Recipe recipe);
    Long countRecipeLikeByRecipe(Recipe recipe);
}
