package pjwstk.aidietgenerator.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjwstk.aidietgenerator.entity.Post;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    PostImage findBypost(Post post);
}
