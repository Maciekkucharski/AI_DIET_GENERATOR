package pjwstk.aidietgenerator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.entity.*;
import pjwstk.aidietgenerator.exception.ResourceNotFoundException;
import pjwstk.aidietgenerator.repository.*;
import pjwstk.aidietgenerator.request.CommentRequest;
import pjwstk.aidietgenerator.request.PostRequest;
import pjwstk.aidietgenerator.view.*;

import javax.persistence.NoResultException;
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
    private final FollowRepository followRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public ForumService(PostRepository postRepository,
                        UserDetailsService userDetailsService,
                        UserRepository userRepository,
                        PostCommentsRepository postCommentsRepository,
                        PostLikesRepository postLikesRepository,
                        RecipeRepository recipeRepository,
                        RecipeLikesRepository recipeLikesRepository,
                        RecipeCommentsRepository recipeCommentsRepository,
                        RecipeService recipeService,
                        FollowRepository followRepository,
                        IngredientRepository ingredientRepository) {
        this.postRepository = postRepository;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.postCommentsRepository = postCommentsRepository;
        this.postLikesRepository = postLikesRepository;
        this.recipeRepository = recipeRepository;
        this.recipeLikesRepository = recipeLikesRepository;
        this.recipeCommentsRepository = recipeCommentsRepository;
        this.recipeService = recipeService;
        this.followRepository = followRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public List<List<PostSimplifiedView>> findAllSimplifiedPosts(HttpServletResponse response) {
        User currentUser = userDetailsService.findCurrentUser();
        List<PostSimplifiedView> postSimplifiedViewList = new ArrayList<>();
        List<PostSimplifiedView> followingPostList = new ArrayList<>();
        List<Follow> followList = new ArrayList<>();
        if(currentUser != null) {
            followList = followRepository.findByFollower(currentUser);
        }
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
                postSimplifiedView.setTimestamp(post.getTimestamp());
                postSimplifiedView.setPostComments(postCommentsRepository.findBypost(post));
                postSimplifiedView.setPostLikes(postLikesRepository.findBypost(post));
                postSimplifiedView.setPostImagePath(post.getImagePath());
                postSimplifiedView.setTitle(post.getTitle());
                postSimplifiedView.setUserProfilePicture(post.getUser().getImagePath());
                if(currentUser != null){
                    for(Follow follow : followList){
                        if(post.getUser() == follow.getUser()) {
                            followingPostList.add(postSimplifiedView);
                        }
                    }
                    postSimplifiedViewList.add(postSimplifiedView);
                } else {
                    postSimplifiedViewList.add(postSimplifiedView);
                }
            }
            List<List<PostSimplifiedView>> listOfListsToReturn = new ArrayList<>();
            postSimplifiedViewList.removeAll(followingPostList);
            listOfListsToReturn.add(followingPostList);
            listOfListsToReturn.add(postSimplifiedViewList);
            response.setStatus(HttpStatus.OK.value());
            return listOfListsToReturn;
        }
    }

    public PostDetailedView viewPost(long postId, HttpServletResponse response) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        } else {
            List<CommentView> postCommentsView = new ArrayList<>();
            for (PostComment comment : postCommentsRepository.findBypost(post.get())) {
                CommentView newCommentView = new CommentView(
                        comment.getId(),
                        comment.getContent(),
                        comment.getTimestamp(),
                        comment.getUser(),
                        comment.getUser().getImagePath()
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
                    post.get().getUser().getImagePath(),
                    postCommentsView,
                    likes);
        }
    }

    public PostDetailedView viewPostByPost(Post post) {
            List<CommentView> postCommentsView = new ArrayList<>();
            for (PostComment comment : postCommentsRepository.findBypost(post)) {
                CommentView newCommentView = new CommentView(
                        comment.getId(),
                        comment.getContent(),
                        comment.getTimestamp(),
                        comment.getUser(),
                        comment.getUser().getImagePath()
                );
                postCommentsView.add(newCommentView);
            }
            List<PostLike> likes = postLikesRepository.findBypost(post);
            return new PostDetailedView(post.getId(),
                    post.getTitle(),
                    post.getDescription(),
                    post.getTimestamp(),
                    post.getImagePath(),
                    post.getUser(),
                    post.getUser().getImagePath(),
                    postCommentsView,
                    likes);

    }

    public Post createPost(PostRequest post, HttpServletResponse response) {
        User currentUser = userDetailsService.findCurrentUser();
        if (currentUser == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        } else {
            if (post.getDescription() == null || post.getTitle() == null) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return null;
            } else {
                Post newPost = new Post();
                newPost.setTitle(post.getTitle());
                newPost.setImagePath(post.getImagePath());
                newPost.setUser(currentUser);
                newPost.setCreatedAt();
                newPost.setDescription(post.getDescription());
                response.setStatus(HttpStatus.CREATED.value());
                return postRepository.save(newPost);
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
            if (post.getImagePath() != null) existingPost.setImagePath(post.getImagePath());
            postRepository.save(existingPost);
            response.setStatus(HttpStatus.OK.value());
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    public Boolean likePost(long postId, HttpServletResponse response) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id :" + postId));
        User currentUser = userDetailsService.findCurrentUser();
        if (currentUser != null) {
            PostLike existingLike = postLikesRepository.findByUserAndPost(currentUser, existingPost);
            if (existingLike != null) {
                postLikesRepository.delete(existingLike);
                response.setStatus(HttpStatus.OK.value());
                return false;
            } else {
                PostLike newLike = new PostLike();
                newLike.setPost(existingPost);
                newLike.setUser(currentUser);
                newLike.setTimestamp(new Timestamp(System.currentTimeMillis()));
                postLikesRepository.save(newLike);
                response.setStatus(HttpStatus.OK.value());
                return true;
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        }
    }

    public PostComment addPostComment(long postId, CommentRequest request, HttpServletResponse response) {
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
                response.setStatus(HttpStatus.OK.value());
                return postCommentsRepository.save(newComment);
            } else {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return null;
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
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

    public List<List<Recipe>> findSimplifiedRecipes(HttpServletResponse response, String option) {
        User currentUser = userDetailsService.findCurrentUser();
        List<Follow> followList = new ArrayList<>();
        List<Recipe> followingRecipeList = new ArrayList<>();
        if(currentUser != null) {
            followList = followRepository.findByFollower(currentUser);
        }
        List<Recipe> allRecipes = new ArrayList<>();
        if (Objects.equals(option, "all")) {
            allRecipes = recipeRepository.findByUserNotNull();
        } else if (Objects.equals(option, "verified")) {
            allRecipes = recipeRepository.findByVerifiedTrueAndUserNotNull();
        } else if (Objects.equals(option, "notVerified")) {
            allRecipes = recipeRepository.findByVerifiedFalseAndUserNotNull();
        }
        if (allRecipes.isEmpty()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        } else {
            for (Recipe recipe : allRecipes) {
                if(currentUser != null && Objects.equals(option, "all")){
                    for(Follow follow : followList){
                        if(recipe.getUser() == follow.getUser()) {
                            followingRecipeList.add(recipe);
                        }
                    }
                }
            }
            List<List<Recipe>> listOfListsToReturn = new ArrayList<>();
            allRecipes.removeAll(followingRecipeList);
            listOfListsToReturn.add(followingRecipeList);
            listOfListsToReturn.add(allRecipes);
            response.setStatus(HttpStatus.OK.value());
            return listOfListsToReturn;
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

    public void follow(long userID, HttpServletResponse response) {
        User currentUser = userDetailsService.findCurrentUser();
        Optional<User> userToFollow = userRepository.findById(userID);
        if (userToFollow.isPresent()) {
            Follow existingFollow = followRepository.findByUserAndFollower(userToFollow.get(), currentUser);
            if (existingFollow != null) {
                followRepository.delete(existingFollow);
                response.setStatus(HttpStatus.OK.value());
            } else if (currentUser != userToFollow.get()) {
                Follow newFollow = new Follow();
                newFollow.setFollower(currentUser);
                newFollow.setUser(userToFollow.get());
                followRepository.save(newFollow);
                response.setStatus(HttpStatus.OK.value());
            } else {
                response.setStatus(HttpStatus.CONFLICT.value());
            }
        } else {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
    }
}