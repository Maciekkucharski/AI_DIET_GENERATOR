package pjwstk.aidietgenerator.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pjwstk.aidietgenerator.entity.User;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class FeedView {
    List<PostDetailedView> newPosts;
    List<RecipeDetailedView> creatorRecipes;
    List<RecipeDetailedView> recipesSortedByLikes;
    @JsonIgnoreProperties({"userPosts", "userRecipes"})
    List<UserProfile> creators;
}
