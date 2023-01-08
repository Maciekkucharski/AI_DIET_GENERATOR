package pjwstk.aidietgenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pjwstk.aidietgenerator.entity.Post;
import pjwstk.aidietgenerator.entity.Recipe;
import pjwstk.aidietgenerator.repository.PostRepository;
import pjwstk.aidietgenerator.repository.RecipeRepository;
import pjwstk.aidietgenerator.request.CommentRequest;
import pjwstk.aidietgenerator.request.PostRequest;
import pjwstk.aidietgenerator.service.PostService;
import pjwstk.aidietgenerator.view.PostDetailedView;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/forum")
public class PostController {

    private final PostRepository postRepository;
    private final RecipeRepository recipeRepository;
    private final PostService postService;

    @Autowired
    public PostController(PostRepository postRepository,
                          PostService postService,
                          RecipeRepository recipeRepository) {
        this.postRepository = postRepository;
        this.postService = postService;
        this.recipeRepository = recipeRepository;
    }

    @GetMapping("/post")
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @GetMapping("/post/like/{postID}")
    @Transactional
    public void likePost(@PathVariable(value = "postID") long postId, HttpServletResponse response) {
        postService.likePost(postId, response);
    }

    @PostMapping("/post/comment/{postID}")
    @Transactional
    public void commentPost(@PathVariable(value = "postID") long postId, @RequestBody CommentRequest request, HttpServletResponse response) {
        postService.addPostComment(postId, request, response);
    }

    @DeleteMapping("/post/comment/delete/{commentID)")
    @Transactional
    public void deletePostComment(@PathVariable(value = "commentID") long commentID, HttpServletResponse response) {
        postService.removePostComment(commentID, response);
    }

    @GetMapping("/post/{postID}")
    public PostDetailedView getPostById(@PathVariable(value = "postID") long postId, HttpServletResponse response) {
        return postService.viewPost(postId, response);
    }

    @GetMapping("/post/user/{userID}")
    public List<Post> getAllUserPosts(@PathVariable(value = "userID") long userId, HttpServletResponse response) {
        return postService.getSelectedUserPosts(userId, response);
    }

    @PostMapping("/post")
    @Transactional
    public void createPost(@RequestBody PostRequest postRequest, HttpServletResponse response) {
        postService.createPost(postRequest, response);
    }

    @PutMapping("/post/{postID}")
    @Transactional
    public void editPost(@RequestBody PostRequest post, @PathVariable("postID") long postId, HttpServletResponse response) {
        postService.editPost(post, response, postId);
    }

    @DeleteMapping("/post/{postID}")
    @Transactional
    public void deletePost(HttpServletResponse response, @PathVariable(value = "postID") long postId) {
        postService.deletePost(response, postId);
    }

    @GetMapping("/recipe")
    public List<Recipe> getAllMeals(){
        return recipeRepository.findAll();
    }


    @GetMapping("/recipe/like/{recipeID}")
    @Transactional
    public void likeMeal(@PathVariable(value = "recipeID") long recipeID, HttpServletResponse response) {
        postService.likeRecipe(recipeID, response);
    }

    @PostMapping("/recipe/comment/{recipeID}")
    @Transactional
    public void commentMeal(@PathVariable(value = "recipeID") long recipeID, @RequestBody CommentRequest request, HttpServletResponse response) {
        postService.addRecipeComment(recipeID, request, response);
    }

    @DeleteMapping("/recipe/comment/delete/{commentID)")
    @Transactional
    public void deleteMealComment(@PathVariable(value = "commentID") long commentID, HttpServletResponse response) {
        postService.removeRecipeComment(commentID, response);
    }

    @GetMapping("/recipe/user/{userID}")
    public List<Recipe> findUserRecipes(@PathVariable(value = "userID") long userId, HttpServletResponse response){
        return postService.getSelectedUserRecipes(userId, response);
    }
}
