package pjwstk.aidietgenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pjwstk.aidietgenerator.entity.Post;
import pjwstk.aidietgenerator.repository.PostRepository;
import pjwstk.aidietgenerator.request.PostRequest;
import pjwstk.aidietgenerator.service.PostService;
import pjwstk.aidietgenerator.view.PostView;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostRepository postRepository;
    private final PostService postService;

    @Autowired
    public PostController(PostRepository postRepository, PostService postService) {
        this.postRepository = postRepository;
        this.postService = postService;
    }

    //Get all posts
    @GetMapping
    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }

    @GetMapping("/{id}")
    public PostView getPoemById(@PathVariable(value = "id" ) long postId, HttpServletResponse response){
        return postService.view(postId, response);
    }

    @GetMapping("/user/{userId}")
    public List<Post> getAllUserPosts(@PathVariable(value = "userId") long userId , HttpServletResponse response){
        return postService.getSelectedUserPosts(userId, response);
    }

    @PostMapping
    @Transactional
    public void createPost(@RequestBody PostRequest postRequest, HttpServletResponse response){
        postService.create(postRequest, response);
    }

    @PutMapping("/{id}")
    @Transactional
    public void putPost(@RequestBody PostRequest post, @PathVariable("id") long postId, HttpServletResponse response){
        postService.edit(post, response, postId);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void deletePost(HttpServletResponse response, @PathVariable (value = "id") long postId){
        postService.delete(response, postId);
    }
}
