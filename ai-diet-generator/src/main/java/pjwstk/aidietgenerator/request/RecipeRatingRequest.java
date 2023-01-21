package pjwstk.aidietgenerator.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RecipeRatingRequest {
    private Integer id;
    private Integer value;
}
