package pjwstk.aidietgenerator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.entity.*;
import pjwstk.aidietgenerator.exception.ResourceNotFoundException;
import pjwstk.aidietgenerator.repository.*;
import pjwstk.aidietgenerator.request.CommentRequest;
import pjwstk.aidietgenerator.request.PostRequest;
import pjwstk.aidietgenerator.view.*;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ForumService {

    private final PostRepository postRepository;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PostCommentsRepository postCommentsRepository;
    private final PostLikesRepository postLikesRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeLikesRepository recipeLikesRepository;
    private final RecipeCommentsRepository recipeCommentsRepository;
    private final RecipeService recipeService;

    @Autowired
    public ForumService(PostRepository postRepository,
                        UserDetailsService userDetailsService,
                        UserRepository userRepository,
                        PostCommentsRepository postCommentsRepository,
                        PostLikesRepository postLikesRepository,
                        RecipeRepository recipeRepository,
                        RecipeLikesRepository recipeLikesRepository,
                        RecipeCommentsRepository recipeCommentsRepository,
                        RecipeService recipeService) {
        this.postRepository = postRepository;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.postCommentsRepository = postCommentsRepository;
        this.postLikesRepository = postLikesRepository;
        this.recipeRepository = recipeRepository;
        this.recipeLikesRepository = recipeLikesRepository;
        this.recipeCommentsRepository = recipeCommentsRepository;
        this.recipeService = recipeService;
    }

    public List<PostSimplifiedView> findAllSimplifiedPosts(HttpServletResponse response) {
        List<PostSimplifiedView> postSimplifiedViewList = new ArrayList<>();
        List<Post> allPosts = postRepository.findAll();
        if (allPosts.isEmpty()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        } else {
            for (Post post : allPosts) {
                PostSimplifiedView postSimplifiedView = new PostSimplifiedView();
                postSimplifiedView.setAuthor(post.getUser());
                postSimplifiedView.setId(post.getId());
                postSimplifiedView.setDescription(post.getDescription());
                postSimplifiedView.setTimestamp(new Timestamp(System.currentTimeMillis()));
                postSimplifiedView.setCommentsCount(postCommentsRepository.findBypost(post).size());
                postSimplifiedView.setLikesCount(postLikesRepository.findBypost(post).size());
                postSimplifiedView.setTitle(post.getTitle());
                postSimplifiedViewList.add(postSimplifiedView);
            }
            response.setStatus(HttpStatus.OK.value());
            return postSimplifiedViewList;
        }
    }

    public PostDetailedView viewPost(long postId, HttpServletResponse response) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        } else {
            List<CommentView> postCommentsView = new ArrayList<>();
            for (PostComment comment : postCommentsRepository.findBypost(post.get())){
                CommentView newCommentView = new CommentView(
                        comment.getId(),
                        comment.getContent(),
                        comment.getTimestamp(),
                        comment.getUser(),
                        "TODO" // TODO
                );
                postCommentsView.add(newCommentView);
            }
            List<PostLike> likes = postLikesRepository.findBypost(post.get());
            response.setStatus(HttpStatus.OK.value());
            return new PostDetailedView(post.get().getId(),
                    post.get().getTitle(),
                    post.get().getDescription(),
                    post.get().getTimestamp(),
                    post.get().getImagePath(),
                    post.get().getUser(),
                    "ImagePath TODO",
                    postCommentsView,
                    likes);
        }
    }

    public void createPost(PostRequest post, HttpServletResponse response) {
        User currentUser = userDetailsService.findCurrentUser();
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

    public void deletePost(HttpServletResponse response, long postId) {
        User currentUser = userDetailsService.findCurrentUser();
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id :" + postId));
        if (existingPost == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        } else {
            if (currentUser != null && Objects.equals(currentUser.getId(), existingPost.getUser().getId())) {
                postRepository.delete(existingPost);
                response.setStatus(HttpStatus.ACCEPTED.value());
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            }
        }
    }

    public List<Post> getSelectedUserPosts(long id, HttpServletResponse response) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return postRepository.findByuser(user.get());
        }
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return null;
    }

    public void editPost(PostRequest post, HttpServletResponse response, long postId) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id :" + postId));
        User currentUser = userDetailsService.findCurrentUser();
        if (currentUser == existingPost.getUser()) {
            if (post.getTitle() != null) existingPost.setTitle(post.getTitle());
            if (post.getDescription() != null) existingPost.setDescription(post.getDescription());
            if (post.getImage_path() != null) existingPost.setImagePath(post.getImage_path());
            postRepository.save(existingPost);
            response.setStatus(HttpStatus.OK.value());
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    public void likePost(long postId, HttpServletResponse response) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id :" + postId));
        User currentUser = userDetailsService.findCurrentUser();
        if (currentUser != null) {
            PostLike existingLike = postLikesRepository.findByUserAndPost(currentUser, existingPost);
            if (existingLike != null) {
                postLikesRepository.delete(existingLike);
                response.setStatus(HttpStatus.OK.value());
            } else {
                PostLike newLike = new PostLike();
                newLike.setPost(existingPost);
                newLike.setUser(currentUser);
                newLike.setTimestamp(new Timestamp(System.currentTimeMillis()));
                postLikesRepository.save(newLike);
                response.setStatus(HttpStatus.OK.value());
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    public void addPostComment(long postId, CommentRequest request, HttpServletResponse response) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id :" + postId));
        User currentUser = userDetailsService.findCurrentUser();
        if (currentUser != null) {
            if (request.getContent() != null || request.getContent().length() < 5) {
                PostComment newComment = new PostComment();
                newComment.setPost(existingPost);
                newComment.setUser(currentUser);
                newComment.setContent(request.getContent());
                newComment.setTimestamp(new Timestamp(System.currentTimeMillis()));
                postCommentsRepository.save(newComment);
                response.setStatus(HttpStatus.OK.value());
            } else {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    public void removePostComment(long commentID, HttpServletResponse response) {
        PostComment existingComment = postCommentsRepository.findById(commentID)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id :" + commentID));
        User currentUser = userDetailsService.findCurrentUser();
        if (currentUser == existingComment.getUser()) {
            postCommentsRepository.delete(existingComment);
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    public List<RecipeSimplifiedView> findSimplifiedRecipes(HttpServletResponse response, String option) {
        List<RecipeSimplifiedView> recipeSimplifiedViewList = new ArrayList<>();
        List<Recipe> allRecipes = new ArrayList<>();
        if(Objects.equals(option, "all")) {
            allRecipes = recipeRepository.findByUserNotNull();
        } else if(Objects.equals(option, "verified")){
            allRecipes = recipeRepository.findByVerifiedTrueAndUserNotNull();
        } else if(Objects.equals(option, "notVerified")){
            allRecipes = recipeRepository.findByVerifiedFalseAndUserNotNull();
        }
        if (allRecipes.isEmpty()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        } else {
            for (Recipe recipe : allRecipes) {
                RecipeSimplifiedView newRecipeSimplifiedView = new RecipeSimplifiedView();
                newRecipeSimplifiedView.setId(recipe.getId());
                newRecipeSimplifiedView.setTitle(recipe.getTitle());
                newRecipeSimplifiedView.setTimestamp(recipe.getTimestamp());
                newRecipeSimplifiedView.setDescription(recipe.getInstructions());
                newRecipeSimplifiedView.setAuthor(recipe.getUser());
                newRecipeSimplifiedView.setUserProfilePicture("ImagePath TODO"); // TODO
                newRecipeSimplifiedView.setCommentsCount(recipeCommentsRepository.findByrecipe(recipe).size());
                newRecipeSimplifiedView.setLikesCount(recipeLikesRepository.findByrecipe(recipe).size());
                recipeSimplifiedViewList.add(newRecipeSimplifiedView);
            }
            response.setStatus(HttpStatus.OK.value());
            return recipeSimplifiedViewList;
        }
    }

    public RecipeDetailedView viewRecipe(long recipeID, HttpServletResponse response) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeID);
        if (recipe.isEmpty()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        } else {
            RecipeDetailedView detailedRecipe = new RecipeDetailedView();
            RecipeView view = recipeService.view(recipeID, response);
            detailedRecipe.setRecipeView(view);
            detailedRecipe.setImagePath("ImagePath TODO"); // TODO
            detailedRecipe.setAuthor(recipe.get().getUser());
            detailedRecipe.setUserProfilePicture("ImagePath TODO"); // TODO
            detailedRecipe.setRecipeLikes(recipeLikesRepository.findByrecipe(recipe.get()));
            List<CommentView> recipeCommentsView = new ArrayList<>();
            for (RecipeComment comment : recipeCommentsRepository.findByrecipe(recipe.get())){
                CommentView newCommentView = new CommentView(
                        comment.getId(),
                        comment.getContent(),
                        comment.getTimestamp(),
                        comment.getUser(),
                        "TODO" // TODO
                );
                recipeCommentsView.add(newCommentView);
            }
            detailedRecipe.setRecipeComments(recipeCommentsView);
            response.setStatus(HttpStatus.OK.value());
            return detailedRecipe;
        }
    }

    public void likeRecipe(HttpServletResponse response, long recipeID) {
        Recipe existingRecipe = recipeRepository.findById(recipeID)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id :" + recipeID));
        User currentUser = userDetailsService.findCurrentUser();
        if (currentUser != null) {
            RecipeLike existingLike = recipeLikesRepository.findByUserAndRecipe(currentUser, existingRecipe);
            if (existingLike != null) {
                recipeLikesRepository.delete(existingLike);
                response.setStatus(HttpStatus.OK.value());
            } else {
                RecipeLike newLike = new RecipeLike();
                newLike.setRecipe(existingRecipe);
                newLike.setUser(currentUser);
                newLike.setTimestamp(new Timestamp(System.currentTimeMillis()));
                recipeLikesRepository.save(newLike);
                response.setStatus(HttpStatus.OK.value());
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    public void addRecipeComment(long recipeID, CommentRequest request, HttpServletResponse response) {
        Recipe existingRecipe = recipeRepository.findById(recipeID)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id :" + recipeID));
        User currentUser = userDetailsService.findCurrentUser();
        if (currentUser != null) {
            if (request.getContent() != null || request.getContent().length() < 5) {
                RecipeComment newComment = new RecipeComment();
                newComment.setRecipe(existingRecipe);
                newComment.setUser(currentUser);
                newComment.setContent(request.getContent());
                newComment.setTimestamp(new Timestamp(System.currentTimeMillis()));
                recipeCommentsRepository.save(newComment);
                response.setStatus(HttpStatus.OK.value());
            } else {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    public void removeRecipeComment(long commentID, HttpServletResponse response) {
        RecipeComment existingComment = recipeCommentsRepository.findById(commentID)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id :" + commentID));
        User currentUser = userDetailsService.findCurrentUser();
        if (currentUser == existingComment.getUser()) {
            recipeCommentsRepository.delete(existingComment);
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    public List<Recipe> getSelectedUserRecipes(long id, HttpServletResponse response) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return recipeRepository.findByuser(user.get());
        }
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return null;
    }

}