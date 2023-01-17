package pjwstk.aidietgenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pjwstk.aidietgenerator.entity.Post;
import pjwstk.aidietgenerator.entity.Recipe;
import pjwstk.aidietgenerator.request.CommentRequest;
import pjwstk.aidietgenerator.request.PostRequest;
import pjwstk.aidietgenerator.service.ForumService;
import pjwstk.aidietgenerator.view.PostDetailedView;
import pjwstk.aidietgenerator.view.PostSimplifiedView;
import pjwstk.aidietgenerator.view.RecipeDetailedView;
import pjwstk.aidietgenerator.view.RecipeSimplifiedView;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/forum")
public class ForumController {

    private final ForumService forumService;

    @Autowired
    public ForumController(ForumService forumService) {
        this.forumService = forumService;
    }

    @GetMapping("/post")
    public List<List<PostSimplifiedView>> viewAllSimplifiedPosts(HttpServletResponse response) {
        return forumService.findAllSimplifiedPosts(response);
    }

    @GetMapping("/post/like/{postID}")
    @Transactional
    public void likePost(@PathVariable(value = "postID") long postId, HttpServletResponse response) {
        forumService.likePost(postId, response);
    }

    @PostMapping("/post/comment/{postID}")
    @Transactional
    public void commentPost(@PathVariable(value = "postID") long postId, @RequestBody CommentRequest request, HttpServletResponse response) {
        forumService.addPostComment(postId, request, response);
    }

    @DeleteMapping("/post/comment/delete/{commentID)")
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
    public void createPost(@RequestBody PostRequest postRequest, HttpServletResponse response) {
        forumService.createPost(postRequest, response);
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
    public List<List<RecipeSimplifiedView>> viewAllSimplifiedRecipes(HttpServletResponse response, String option){
        return forumService.findSimplifiedRecipes(response, "all");
    }

    @GetMapping("/recipe/verified")
    public List<List<RecipeSimplifiedView>> viewVerifiedRecipes(HttpServletResponse response, String option){
        return forumService.findSimplifiedRecipes(response, "verified");
    }

    @GetMapping("/recipe/notVerified")
    public List<List<RecipeSimplifiedView>> viewNotVerifiedRecipes(HttpServletResponse response, String option){
        return forumService.findSimplifiedRecipes(response, "notVerified");
    }

    @GetMapping("/recipe/{recipeID}")
    public RecipeDetailedView viewDetailedRecipe(@PathVariable(value = "recipeID") long recipeID, HttpServletResponse response) {
        return forumService.viewRecipe(recipeID, response);
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

    @DeleteMapping("/recipe/comment/delete/{commentID)")
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
