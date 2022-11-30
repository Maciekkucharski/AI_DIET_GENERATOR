package pjwstk.aidietgenerator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.entity.Comment;
import pjwstk.aidietgenerator.entity.Post;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.exception.ResourceNotFoundException;
import pjwstk.aidietgenerator.repository.CommentRepository;
import pjwstk.aidietgenerator.repository.PostRepository;
import pjwstk.aidietgenerator.repository.UserRepository;
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
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public PostService(PostRepository postRepository,
                       UserService userService,
                       UserRepository userRepository,
                       CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public PostView view(long postId, HttpServletResponse response) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        } else {
            HashMap<Long, String> postComments = new HashMap<>();
            try {
                postComments = getComments(post.get());
            } catch (NoResultException e) {
                e.printStackTrace();
            }
            response.setStatus(HttpStatus.OK.value());
            return new PostView(post.get().getId(),
                    post.get().getTitle()
                    , post.get().getDescription()
                    , post.get().getTimestamp()
                    , post.get().getImagePath()
                    , postComments);
        }
    }

    public Post create(PostRequest post, HttpServletResponse response) {
        User currentUser = userService.findCurrentUser();
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
                return newPost;
            }
        }
        return null;
    }

    public void delete(HttpServletResponse response, long postId) {
        User currentUser = userService.findCurrentUser();
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

    public HashMap<Long, String> getComments(Post post) {
        HashMap<Long, String> comments = new HashMap<>();
        List<Comment> commentValues =  commentRepository.findBypost(post);
        for (Comment comment : commentValues) {
            comments.put(comment.getId(), comment.getContent());
        }
        return comments;
    }

    public List<Post> getSelectedUserPosts(long id, HttpServletResponse response) {
        Optional<User> user = userRepository.findById(id);
        if(!user.isEmpty()) {
            return postRepository.findByuser(user.get());
        }
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return null;
    }

    public Post edit(PostRequest post, HttpServletResponse response, long postId) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id :" + postId));
        User currentUser = userService.findCurrentUser();
        if(currentUser == existingPost.getUser()) {
            if (post.getTitle() != null) existingPost.setTitle(post.getTitle());
            if (post.getDescription() != null) existingPost.setDescription(post.getDescription());
            if (post.getImage_path() != null) existingPost.setImagePath(post.getImage_path());
            postRepository.save(existingPost);
            response.setStatus(HttpStatus.OK.value());
            return existingPost;
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        }
    }
}