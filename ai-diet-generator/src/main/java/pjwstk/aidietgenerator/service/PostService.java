package pjwstk.aidietgenerator.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.entity.Comment;
import pjwstk.aidietgenerator.entity.Post;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.repository.PostRepository;
import pjwstk.aidietgenerator.request.PostRequest;
import pjwstk.aidietgenerator.view.PostView;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    @PersistenceContext
    private final EntityManager entityManager;

    public PostService(PostRepository postRepository, EntityManager entityManager) {
        this.postRepository = postRepository;
        this.entityManager = entityManager;
    }

    public PostView view(long postId, HttpServletResponse response) {
        Post post = entityManager.find(Post.class, postId);
        if (post == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        } else {
            HashMap<Long, String> postComments = new HashMap<>();
            try {
                postComments = getComments(post);
            } catch (NoResultException e) {
                e.printStackTrace();
            }
            response.setStatus(HttpStatus.OK.value());
            return new PostView(post.getId(), post.getTitle(), post.getDescription(), post.getTimestamp(), postComments);
        }
    }

    public void create(PostRequest post, HttpServletResponse response) {
        User currentUser = findCurrentUser();
        if (currentUser == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            if (post.getDescription() == null || post.getTitle() == null) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
            } else {
                Post newPost = new Post();
                newPost.setTitle(post.getTitle());
                newPost.setImagePath(post.getImage_path());
                newPost.setUser(currentUser);
                newPost.setCreatedAt();
                newPost.setDescription(post.getDescription());
                postRepository.save(newPost);
                response.setStatus(HttpStatus.CREATED.value());
            }
        }
    }

//    public Post edit(Post post, HttpServletResponse response, long postId) {
//
//    }

    public void delete(HttpServletResponse response, long postId) {
        User currentUser = findCurrentUser();
        Post existingPost = postRepository.getReferenceById(postId);
        if (existingPost == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        } else {
            if (currentUser != null && currentUser.getId() == existingPost.getUser().getId()) {
                postRepository.delete(existingPost);
                response.setStatus(HttpStatus.ACCEPTED.value());
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            }
        }
    }

    public String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            String username = ((User) principal).getUsername();
            return username;
        } else {
            return principal.toString();
        }
    }

    private User findCurrentUser() {
        User currentUser = null;
        try {
            currentUser = entityManager.createQuery("SELECT user FROM User user WHERE user.username= :username", User.class).setParameter("username", getCurrentUsername()).getSingleResult();
        } catch (NoResultException e) {
            System.out.println(e.getMessage());
        }
        return currentUser;
    }

    public HashMap<Long, String> getComments(Post post) {
        HashMap<Long, String> comments = new HashMap<>();
        List<Comment> commentValues = entityManager.createQuery("SELECT comments FROM Comment comments WHERE comments.post.id = :id", Comment.class).setParameter("id", post.getId()).getResultList();
        for (Comment comment : commentValues) {
            comments.put(comment.getId(), comment.getContent());
        }
        return comments;
    }
}