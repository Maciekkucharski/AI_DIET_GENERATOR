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

    // TODO
    private String imagePath = "TODO";

    @JsonIgnoreProperties({"authorities", "username", "email", "authority"})
    private User author;

    // TODO
    private String userProfilePicture = "TODO";

    private List<CommentView> recipeComments;
    private List<RecipeLike> recipeLikes;
}
