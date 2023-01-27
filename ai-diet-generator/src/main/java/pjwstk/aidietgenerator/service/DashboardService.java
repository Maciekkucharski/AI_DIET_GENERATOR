package pjwstk.aidietgenerator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.entity.Post;
import pjwstk.aidietgenerator.entity.Recipe;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.repository.*;
import pjwstk.aidietgenerator.view.FeedView;
import pjwstk.aidietgenerator.view.PostDetailedView;
import pjwstk.aidietgenerator.view.UserProfile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DashboardService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final ProfileService profileService;
    private final ForumService forumService;
    private final RecipeLikesRepository recipeLikesRepository;

    @Autowired
    public DashboardService(PostRepository postRepository,
                            UserRepository userRepository,
                            RecipeRepository recipeRepository,
                            ProfileService profileService,
                            ForumService forumService,
                            RecipeLikesRepository recipeLikesRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.profileService = profileService;
        this.forumService = forumService;
        this.recipeLikesRepository = recipeLikesRepository;
    }

    public FeedView loadFeed(HttpServletResponse response) {
        List<UserProfile> dietInflu = new ArrayList<>();
        List<PostDetailedView> postViewList = new ArrayList<>();
        FeedView newFeed = new FeedView();

        List<User> allUser = userRepository.findAll();
        for(User user: allUser){
            if(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_INFLUENCER")) ||
                    user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DIETITIAN"))){
                UserProfile newProfile = profileService.getSelectedUserProfile(user.getId(), response);
                dietInflu.add(newProfile);
            }
        }
        newFeed.setCreators(dietInflu);

        List<Recipe> recipesWithUser = recipeRepository.findByUserNotNull();
        List<Recipe> creatorREcipes = new ArrayList<>();
        for(Recipe recipe: recipesWithUser){
                if(recipe.getUser().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_INFLUENCER")) ||
                    recipe.getUser().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DIETITIAN")))
                    creatorREcipes.add(recipe);
            }
        newFeed.setCreatorRecipes(creatorREcipes);

        List<Post> allPosts = postRepository.findFirst3ByOrderByIdDesc();
        Collections.reverse(allPosts);
        for(Post post: allPosts){
            PostDetailedView postDetailedView = forumService.viewPostByPost(post);
            postViewList.add(postDetailedView);
        }
        newFeed.setNewPosts(postViewList);

        List<Long> bestRecipesID = recipeRepository.findTopTenLikedRecipes();
        List<Recipe> bestRecipes = new ArrayList<>();
        for(Long id : bestRecipesID){
            bestRecipes.add(recipeRepository.findById(id).get());
        }
        newFeed.setRecipesSortedByLikes(bestRecipes);
        return newFeed;
    }
}
