package pjwstk.aidietgenerator.request;

import pjwstk.aidietgenerator.entity.Question;

import java.util.List;

public class SurveyRequest {
    private List<String> exclusions;
    private List<Question> answers;

    public SurveyRequest(List<String> exclusions, List<Question> answers) {
        this.exclusions = exclusions;
        this.answers = answers;
    }

    public SurveyRequest() {
    }

    public List<String> getExclusions() {
        return exclusions;
    }

    public List<Question> getAnswers() {
        return answers;
    }
}
