package pjwstk.aidietgenerator.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pjwstk.aidietgenerator.entity.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RecipeDetailedView {
    private RecipeView recipeView;
    private String recipeImagePath;

    @JsonIgnoreProperties({"authorities", "username", "email", "authority"})
    private User author;

    private String profileImagePath;
    private List<CommentView> recipeComments;
    private List<RecipeLike> recipeLikes;
}
