package pjwstk.aidietgenerator.controller;

import org.springframework.web.bind.annotation.*;
import pjwstk.aidietgenerator.entity.Rating;
import pjwstk.aidietgenerator.entity.Survey;
import pjwstk.aidietgenerator.repository.SurveyRepository;
import pjwstk.aidietgenerator.repository.UserRepository;
import pjwstk.aidietgenerator.request.SurveyRatingRequest;
import pjwstk.aidietgenerator.request.SurveyRequest;
import pjwstk.aidietgenerator.service.SurveyService;
import pjwstk.aidietgenerator.service.UserDetailsService;
import pjwstk.aidietgenerator.view.RecipeSurveyView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/account/survey")
@CrossOrigin(exposedHeaders = "*")
public class SurveyController {

    private final SurveyRepository surveyRepository;
    private final UserDetailsService userDetailsService;
    private final SurveyService surveyService;

    public SurveyController(SurveyRepository surveyRepository,
                            UserDetailsService userDetailsService,
                            SurveyService surveyService) {
        this.surveyRepository = surveyRepository;
        this.userDetailsService = userDetailsService;
        this.surveyService = surveyService;
    }

    @GetMapping
    public List<Survey> getCurrentUserSurvey(){
        return surveyRepository.findByuser(userDetailsService.findCurrentUser());
    }

    @PostMapping
    public Survey saveUserSurvey(@RequestBody SurveyRequest surveyRequest, HttpServletResponse response){
        return surveyService.saveSurvey(surveyRequest, response);
    }

    @GetMapping("/rating")
    public List<RecipeSurveyView> get30RecipesToRate(HttpServletResponse response){
        return surveyService.getRecipes(response);
    }

    @PostMapping("/rating")
    public List<Rating> addUserRecipesRatings(@RequestBody SurveyRatingRequest request, HttpServletResponse response){
        return surveyService.saveUserRecipeRatings(request, response);
    }

}
