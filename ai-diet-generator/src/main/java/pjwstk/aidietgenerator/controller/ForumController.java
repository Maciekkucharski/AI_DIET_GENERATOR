package pjwstk.aidietgenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pjwstk.aidietgenerator.entity.Post;
import pjwstk.aidietgenerator.entity.PostComment;
import pjwstk.aidietgenerator.entity.Recipe;
import pjwstk.aidietgenerator.exception.ResourceNotFoundException;
import pjwstk.aidietgenerator.repository.RecipeRepository;
import pjwstk.aidietgenerator.request.CommentRequest;
import pjwstk.aidietgenerator.request.PostRequest;
import pjwstk.aidietgenerator.service.ForumService;
import pjwstk.aidietgenerator.view.PostDetailedView;
import pjwstk.aidietgenerator.view.PostSimplifiedView;
import pjwstk.aidietgenerator.view.RecipeSimplifiedView;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/forum")
@CrossOrigin(exposedHeaders = "*")
public class ForumController {

    private final ForumService forumService;
    private final RecipeRepository recipeRepository;

    @Autowired
    public ForumController(ForumService forumService, RecipeRepository recipeRepository) {
        this.forumService = forumService;
        this.recipeRepository = recipeRepository;
    }

    @GetMapping("/post")
    public List<List<PostSimplifiedView>> viewAllSimplifiedPosts(HttpServletResponse response) {
        return forumService.findAllSimplifiedPosts(response);
    }

    @GetMapping("/post/like/{postID}")
    @Transactional
    public Boolean likePost(@PathVariable(value = "postID") long postId, HttpServletResponse response) {
        return forumService.likePost(postId, response);
    }

    @PostMapping("/post/comment/{postID}")
    @Transactional
    public PostComment commentPost(@PathVariable(value = "postID") long postId, @RequestBody CommentRequest request, HttpServletResponse response) {
        return forumService.addPostComment(postId, request, response);
    }

    @DeleteMapping("/post/comment/delete/{commentID})")
    @Transactional
    public void deletePostComment(@PathVariable(value = "commentID") long commentID, HttpServletResponse response) {
        forumService.removePostComment(commentID, response);
    }

    @GetMapping("/post/{postID}")
    public PostDetailedView viewDetailedPost(@PathVariable(value = "postID") long postId, HttpServletResponse response) {
        return forumService.viewPost(postId, response);
    }

    @GetMapping("/post/user/{userID}")
    public List<Post> getAllUserPosts(@PathVariable(value = "userID") long userId, HttpServletResponse response) {
        return forumService.getSelectedUserPosts(userId, response);
    }

    @PostMapping("/post")
    @Transactional
    public Post createPost(@RequestBody PostRequest postRequest, HttpServletResponse response) {
        return forumService.createPost(postRequest, response);
    }

    @PutMapping("/post/{postID}")
    @Transactional
    public void editPost(@RequestBody PostRequest post, @PathVariable("postID") long postId, HttpServletResponse response) {
        forumService.editPost(post, response, postId);
    }

    @DeleteMapping("/post/{postID}")
    @Transactional
    public void deletePost(@PathVariable(value = "postID") long postId, HttpServletResponse response) {
        forumService.deletePost(response, postId);
    }

    @GetMapping("/recipe")
    public List<List<Recipe>> viewAllSimplifiedRecipes(HttpServletResponse response){
        return forumService.findSimplifiedRecipes(response, "all");
    }

    @GetMapping("/recipe/verified")
    public List<List<Recipe>> viewVerifiedRecipes(HttpServletResponse response){
        return forumService.findSimplifiedRecipes(response, "verified");
    }

    @GetMapping("/recipe/notVerified")
    public List<List<Recipe>> viewNotVerifiedRecipes(HttpServletResponse response){
        return forumService.findSimplifiedRecipes(response, "notVerified");
    }

    @GetMapping("/recipe/{recipeID}")
    public Recipe viewDetailedRecipe(@PathVariable(value = "recipeID") long recipeID) {
        return recipeRepository.findById(recipeID)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id :" + recipeID));
    }
    @GetMapping("/recipe/like/{recipeID}")
    @Transactional
    public void likeMeal(@PathVariable(value = "recipeID") long recipeID, HttpServletResponse response) {
        forumService.likeRecipe(response, recipeID);
    }

    @PostMapping("/recipe/comment/{recipeID}")
    @Transactional
    public void commentMeal(@PathVariable(value = "recipeID") long recipeID, @RequestBody CommentRequest request, HttpServletResponse response) {
        forumService.addRecipeComment(recipeID, request, response);
    }

    @DeleteMapping("/recipe/comment/delete/{commentID}")
    @Transactional
    public void deleteMealComment(@PathVariable(value = "commentID") long commentID, HttpServletResponse response) {
        forumService.removeRecipeComment(commentID, response);
    }

    @GetMapping("/recipe/user/{userID}")
    public List<Recipe> findUserRecipes(@PathVariable(value = "userID") long userId, HttpServletResponse response){
        return forumService.getSelectedUserRecipes(userId, response);
    }

    @GetMapping("/follow/{userID}")
    public void followUser(@PathVariable(value = "userID") long userID, HttpServletResponse response){
        forumService.follow(userID, response);
    }
}
