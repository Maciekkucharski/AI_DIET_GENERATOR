package pjwstk.aidietgenerator.imageGCS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjwstk.aidietgenerator.imageGCS.entity.RecipeImage;

@Repository
public interface RecipeImageRepository extends JpaRepository<RecipeImage, Long> {
}
