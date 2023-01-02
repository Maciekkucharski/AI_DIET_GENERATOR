package pjwstk.aidietgenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pjwstk.aidietgenerator.entity.Post;
import pjwstk.aidietgenerator.repository.PostRepository;
import pjwstk.aidietgenerator.request.CommentRequest;
import pjwstk.aidietgenerator.request.PostRequest;
import pjwstk.aidietgenerator.service.PostService;
import pjwstk.aidietgenerator.view.PostDetailedView;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/forum/post")
public class PostController {

    private final PostRepository postRepository;
    private final PostService postService;

    @Autowired
    public PostController(PostRepository postRepository, PostService postService) {
        this.postRepository = postRepository;
        this.postService = postService;
    }

    @GetMapping
    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }

    @GetMapping("/like/{postID}")
    public void likePost(@PathVariable(value = "postID" ) long postId, HttpServletResponse response){
        postService.like(postId, response);
    }

    @PostMapping("/comment/{postID}")
    public void commentPost(@PathVariable(value = "postID" ) long postId, @RequestBody CommentRequest request, HttpServletResponse response){
        postService.addComment(postId, request, response);
    }

    @GetMapping("/{postID}")
    public PostDetailedView getPostById(@PathVariable(value = "postID" ) long postId, HttpServletResponse response){
        return postService.view(postId, response);
    }

    @GetMapping("/user/{userID}")
    public List<Post> getAllUserPosts(@PathVariable(value = "userID") long userId , HttpServletResponse response){
        return postService.getSelectedUserPosts(userId, response);
    }

    @PostMapping
    @Transactional
    public void createPost(@RequestBody PostRequest postRequest, HttpServletResponse response){
        postService.create(postRequest, response);
    }

    @PutMapping("/{postID}")
    @Transactional
    public void putPost(@RequestBody PostRequest post, @PathVariable("postID") long postId, HttpServletResponse response){
        postService.edit(post, response, postId);
    }

    @DeleteMapping("/{postID}")
    @Transactional
    public void deletePost(HttpServletResponse response, @PathVariable (value = "postID") long postId){
        postService.delete(response, postId);
    }
}
