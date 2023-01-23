package pjwstk.aidietgenerator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.entity.Post;
import pjwstk.aidietgenerator.entity.Recipe;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.repository.*;
import pjwstk.aidietgenerator.view.FeedView;
import pjwstk.aidietgenerator.view.PostDetailedView;
import pjwstk.aidietgenerator.view.RecipeDetailedView;
import pjwstk.aidietgenerator.view.UserProfile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class DashboardService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final ProfileService profileService;
    private final ForumService forumService;
    @Autowired
    public DashboardService(PostRepository postRepository,
                            UserRepository userRepository,
                            RecipeRepository recipeRepository,
                            ProfileService profileService,
                            ForumService forumService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.profileService = profileService;
        this.forumService = forumService;
    }

    public FeedView loadFeed(HttpServletResponse response) {
        List<UserProfile> dietInflu = new ArrayList<>();
        List<PostDetailedView> postViewList = new ArrayList<>();
        List<RecipeDetailedView> recipeViewList = new ArrayList<>();
        List<RecipeDetailedView> bestRecipes = new ArrayList<>();
        FeedView newFeed = new FeedView();

        List<User> allUser = userRepository.findAll();
        for(User user: allUser){
            if(user.getAuthorities().contains("ROLE_DIETITIAN") || user.getAuthorities().contains("ROLE_INFLUENCER")){
                UserProfile newProfile = profileService.getSelectedUserProfile(user.getId(), response);
                dietInflu.add(newProfile);
            }
        }
        newFeed.setCreators(dietInflu);

        List<Recipe> allRecipesWithUser = recipeRepository.findByUserNotNull();
        for(Recipe recipe: allRecipesWithUser){
            if(recipe.getUser().getAuthorities().contains("ROLE_DIETITIAN") || recipe.getUser().getAuthorities().contains("ROLE_INFLUENCER")){
                RecipeDetailedView newDetailedView = forumService.viewRecipe(recipe.getId(), response);
                recipeViewList.add(newDetailedView);
            }
        }
        newFeed.setCreatorRecipes(recipeViewList);

        List<Post> allPosts = postRepository.findAll();
        Collections.reverse(allPosts);
        for(Post post: allPosts){
            PostDetailedView postDetailedView = forumService.viewPost(post.getId(), response);
            postViewList.add(postDetailedView);
        }
        newFeed.setNewPosts(postViewList);

//        List<Recipe> allRecipes = recipeRepository.findAll();
//        for(Recipe recipe: allRecipes){
//                RecipeDetailedView newDetailedView = forumService.viewRecipe(recipe.getId(), response);
//                bestRecipes.add(newDetailedView);
//        }
//
//        Collections.sort(bestRecipes, new Comparator<RecipeDetailedView>(){
//            public int compare(RecipeDetailedView o1, RecipeDetailedView o2){
//                return o1.getRecipeLikes().size() - o2.getRecipeLikes().size();
//            }
//        });
        newFeed.setRecipesSortedByLikes(recipeViewList);
        return newFeed;
    }
}
