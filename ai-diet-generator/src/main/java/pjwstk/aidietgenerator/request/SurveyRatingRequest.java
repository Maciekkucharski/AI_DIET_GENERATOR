package pjwstk.aidietgenerator.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SurveyRatingRequest {
    private List<RecipeRatingRequest> ratingList;
}
