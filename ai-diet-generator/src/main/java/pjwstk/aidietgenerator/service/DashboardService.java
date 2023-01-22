package pjwstk.aidietgenerator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.repository.*;

@Service
public class DashboardService {
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

    @Autowired
    public DashboardService(PostRepository postRepository,
                            UserDetailsService userDetailsService,
                            UserRepository userRepository,
                            PostCommentsRepository postCommentsRepository,
                            PostLikesRepository postLikesRepository,
                            RecipeRepository recipeRepository, RecipeLikesRepository recipeLikesRepository,
                            RecipeCommentsRepository recipeCommentsRepository, RecipeService recipeService,
                            FollowRepository followRepository) {
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
    }
}
