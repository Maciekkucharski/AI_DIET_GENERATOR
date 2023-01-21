package pjwstk.aidietgenerator.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.entity.Question;
import pjwstk.aidietgenerator.entity.Survey;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.repository.SurveyRepository;
import pjwstk.aidietgenerator.repository.UserRepository;
import pjwstk.aidietgenerator.request.SurveyRequest;


import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final UserDetailsService userDetailsService;

    public SurveyService(SurveyRepository surveyRepository,
                         UserDetailsService userDetailsService) {
        this.surveyRepository = surveyRepository;
        this.userDetailsService = userDetailsService;
    }

    public Survey saveSurvey(SurveyRequest surveyRequest, HttpServletResponse response){
        User currentUser = userDetailsService.findCurrentUser();
        Survey survey = new Survey();
        if(currentUser != null) {
            if(surveyRequest.getAnswers().size() == 13) {
                List<Question> answers = surveyRequest.getAnswers();
                for (Question question : answers) {
                    if (question.getId() == 1) survey.setAnswer1(question.getValue());
                    if (question.getId() == 2) survey.setAnswer2(question.getValue());
                    if (question.getId() == 3) survey.setAnswer3(question.getValue());
                    if (question.getId() == 4) survey.setAnswer4(question.getValue());
                    if (question.getId() == 5) survey.setAnswer5(question.getValue());
                    if (question.getId() == 6) survey.setAnswer6(question.getValue());
                    if (question.getId() == 7) survey.setAnswer7(question.getValue());
                    if (question.getId() == 8) survey.setAnswer8(question.getValue());
                    if (question.getId() == 9) survey.setAnswer9(question.getValue());
                    if (question.getId() == 10) survey.setAnswer10(question.getValue());
                    if (question.getId() == 11) survey.setAnswer11(question.getValue());
                    if (question.getId() == 12) survey.setAnswer12(question.getValue());
                    if (question.getId() == 13) survey.setAnswer13(question.getValue());
                }
                survey.setTimestamp(new Timestamp(System.currentTimeMillis()));
                survey.setUser(currentUser);
                response.setStatus(HttpStatus.OK.value());
                return surveyRepository.save(survey);
            } else {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return null;
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        }
    }
}
