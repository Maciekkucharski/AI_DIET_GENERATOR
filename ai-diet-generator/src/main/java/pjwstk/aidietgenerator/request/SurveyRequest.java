package pjwstk.aidietgenerator.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pjwstk.aidietgenerator.entity.Question;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SurveyRequest {
    private List<Question> answers;
}
