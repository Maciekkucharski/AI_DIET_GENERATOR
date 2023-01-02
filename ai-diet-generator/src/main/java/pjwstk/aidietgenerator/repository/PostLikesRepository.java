package pjwstk.aidietgenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjwstk.aidietgenerator.entity.Post;
import pjwstk.aidietgenerator.entity.PostLike;
import pjwstk.aidietgenerator.entity.User;

import java.util.List;

@Repository
public interface PostLikesRepository extends JpaRepository<PostLike, Long> {
    List<PostLike> findBypost(Post post);
    PostLike findByUserAndPost(User user, Post post);
}
