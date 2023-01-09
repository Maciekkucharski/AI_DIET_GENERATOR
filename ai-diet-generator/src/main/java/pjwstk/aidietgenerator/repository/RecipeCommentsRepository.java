package pjwstk.aidietgenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjwstk.aidietgenerator.entity.PostComment;
import pjwstk.aidietgenerator.entity.Recipe;
import pjwstk.aidietgenerator.entity.RecipeComment;

import java.util.List;

@Repository
public interface RecipeCommentsRepository extends JpaRepository<RecipeComment, Long> {
    List<RecipeComment> findByrecipe(Recipe recipe);
}
