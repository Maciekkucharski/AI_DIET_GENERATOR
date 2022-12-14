package pjwstk.aidietgenerator.controller;

import org.springframework.web.bind.annotation.*;
import pjwstk.aidietgenerator.entity.Survey;
import pjwstk.aidietgenerator.repository.SurveyRepository;
import pjwstk.aidietgenerator.repository.UserRepository;
import pjwstk.aidietgenerator.request.SurveyRequest;
import pjwstk.aidietgenerator.service.SurveyService;
import pjwstk.aidietgenerator.service.UserDetailsService;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/account/survey")
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
    public Survey getCurrentUserSurvey(){
        return surveyRepository.findByuser(userDetailsService.findCurrentUser());
    }

    @PostMapping
    public Survey saveUserSurvey(@RequestBody SurveyRequest surveyRequest, HttpServletResponse response){
        return surveyService.saveSurvey(surveyRequest, response);
    }
}
