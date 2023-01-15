package pjwstk.aidietgenerator.imageGCS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjwstk.aidietgenerator.imageGCS.entity.BackgroundImage;

@Repository
public interface BackgroundImageRepository extends JpaRepository<BackgroundImage, Long> {
}
