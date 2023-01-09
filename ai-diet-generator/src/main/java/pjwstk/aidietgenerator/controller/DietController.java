package pjwstk.aidietgenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.repository.RecipeRepository;
import pjwstk.aidietgenerator.service.DietService;
import pjwstk.aidietgenerator.service.UserDetailsService;

import javax.transaction.Transactional;
import java.io.IOException;

@RestController
@RequestMapping("/diet")
public class DietController {

    private final DietService dietService;
    private final RecipeRepository recipeRepository;
    private final UserDetailsService userDetailsService;

    @Autowired
    public DietController(DietService dietService, RecipeRepository recipeRepository, UserDetailsService userDetailsService){
        this.dietService = dietService;
        this.recipeRepository = recipeRepository;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/generate")
    @Transactional
    public void getIds() throws IOException {
        User currentUser = userDetailsService.findCurrentUser();
        dietService.getRecommendedIds(currentUser.getId());
    }
}
