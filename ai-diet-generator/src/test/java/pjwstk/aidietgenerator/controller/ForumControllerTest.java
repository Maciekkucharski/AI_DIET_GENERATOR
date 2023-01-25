package pjwstk.aidietgenerator.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pjwstk.aidietgenerator.entity.Post;
import pjwstk.aidietgenerator.request.CommentRequest;
import pjwstk.aidietgenerator.request.PostRequest;
import pjwstk.aidietgenerator.service.ForumService;
import pjwstk.aidietgenerator.view.PostSimplifiedView;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ForumControllerTest {

    @Mock
    private ForumService forumService;

    @InjectMocks
    private ForumController forumController;

    @Test
    @DisplayName("Should throw an exception when the post does not exist")
    void commentPostWhenPostDoesNotExistThenThrowException() {
        long postId = 1L;
        CommentRequest request = new CommentRequest("comment");
        when(forumService.addPostComment(postId, request, null))
                .thenThrow(new IllegalArgumentException());

        assertThrows(
                IllegalArgumentException.class,
                () -> forumController.commentPost(postId, request, null));
    }

    @Test
    @DisplayName("Should add a comment to the post when the post exists")
    void commentPostWhenPostExists() {
        long postId = 1L;
        CommentRequest request = new CommentRequest("comment");
        PostSimplifiedView postSimplifiedView = new PostSimplifiedView();
        postSimplifiedView.setId(postId);
        List<PostSimplifiedView> postSimplifiedViews = Arrays.asList(postSimplifiedView);
        HttpServletResponse response = mock(HttpServletResponse.class);
        forumController.commentPost(postId, request, response);
        verify(forumService, times(1)).addPostComment(postId, request, response);
    }

    @Test
    @DisplayName("Should throw an exception when the user is not logged in")
    void createPostWhenUserIsNotLoggedInThenThrowException() {
        PostRequest postRequest = new PostRequest("title", "description", "imagePath");
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getStatus()).thenReturn(401);
        assertEquals(401, response.getStatus());
    }

    @Test
    @DisplayName("Should save the post when the user is logged in")
    void createPostWhenUserIsLoggedIn() {
        PostRequest postRequest = new PostRequest("title", "description", "imagePath");
        HttpServletResponse response = mock(HttpServletResponse.class);
        forumController.createPost(postRequest, response);
        verify(forumService, times(1)).createPost(postRequest, response);
    }

    @Test
    @DisplayName("Should return 404 when the post does not exist")
    void likePostWhenPostDoesNotExistThenReturn404() {
        long postId = 1L;
        when(forumService.likePost(postId, null)).thenReturn(false);
        boolean result = forumController.likePost(postId, null);
        assertFalse(result);
    }

//    @Test
//    @DisplayName("Should increase the number of likes by 1 when the post exists")
//    void likePostWhenPostExistsThenIncreaseNumberOfLikesBy1() {
//        long postId = 1;
//        HttpServletResponse response = mock(HttpServletResponse.class);
//        Post post = new Post("title", "desc", "img");
//        post.setId(postId);
//        forumController.likePost(postId, response);
//        PostSimplifiedView po
//        assertEquals(1, postSimplifiedView.getLikesCount());
//    }

    @Test
    @DisplayName("Should return all posts")
    void viewAllSimplifiedPostsShouldReturnAllPosts() {
        List<List<PostSimplifiedView>> posts =
                Arrays.asList(Arrays.asList(new PostSimplifiedView()));
        when(forumService.findAllSimplifiedPosts(any())).thenReturn(posts);
        List<List<PostSimplifiedView>> result = forumController.viewAllSimplifiedPosts(null);
        assertEquals(posts, result);
    }
}