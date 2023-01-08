package pjwstk.aidietgenerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pjwstk.aidietgenerator.entity.PostComment;
import pjwstk.aidietgenerator.entity.Post;

import java.util.List;

@Repository
public interface PostCommentsRepository extends JpaRepository<PostComment, Long> {
    List<PostComment> findBypost(Post post);
}
