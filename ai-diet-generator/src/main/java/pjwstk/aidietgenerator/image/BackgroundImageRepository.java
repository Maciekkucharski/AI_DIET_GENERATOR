package pjwstk.aidietgenerator.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjwstk.aidietgenerator.entity.User;

@Repository
public interface BackgroundImageRepository extends JpaRepository<BackgroundImage, Long> {
    BackgroundImage findByuser(User user);
}
