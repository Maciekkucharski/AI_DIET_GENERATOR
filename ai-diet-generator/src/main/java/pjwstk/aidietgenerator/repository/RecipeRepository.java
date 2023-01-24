package pjwstk.aidietgenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pjwstk.aidietgenerator.entity.Recipe;
import pjwstk.aidietgenerator.entity.User;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByuser(User user);
    List<Recipe> findByUserNotNull();
    List<Recipe> findByUserNull();
    List<Recipe> findByVerifiedTrueAndUserNotNull();
    List<Recipe> findByVerifiedFalseAndUserNotNull();
    List<Recipe> findFirst10ByUserNotNull();

    @Query(value = "SELECT recipes.id, COUNT(*) AS number_of_likes FROM recipes, recipe_likes WHERE recipes.id = recipe_likes.recipe_id GROUP BY recipes.id ORDER BY number_of_likes DESC LIMIT 10",
    nativeQuery = true)
    List<Long> findTopTenLikedRecipes();
}
