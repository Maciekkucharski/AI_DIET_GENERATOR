package pjwstk.aidietgenerator.controller;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.repository.IngredientRepository;
import pjwstk.aidietgenerator.repository.RecipeRepository;
import pjwstk.aidietgenerator.request.RecipeRequest;
import pjwstk.aidietgenerator.service.RecipeService;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {

    @Mock
    private RecipeService recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private RecipeController recipeController;

    @Mock
    private HttpServletResponse response;


    @Test
    @DisplayName("Should throw an exception when the recipe is invalid")
    void addRecipeWhenRecipeIsInvalidThenThrowException() {
        RecipeRequest recipeRequest =
                new RecipeRequest(
                        null,
                        0,
                        0,
                        "",
                        "",
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        new Timestamp(System.currentTimeMillis()),
                        new User("", ""),
                        0,
                        0,
                        0,
                        0,
                        Collections.emptyList());

        recipeController.addRecipe(recipeRequest, response);
        verify(response).setStatus(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Should save the recipe when the recipe is valid")
    void addRecipeWhenRecipeIsValid() {
        RecipeRequest recipeRequest =
                new RecipeRequest(
                        "title",
                        1,
                        1,
                        "imagePath",
                        "instructions",
                        true,
                        true,
                        true,
                        true,
                        true,
                        true,
                        new Timestamp(System.currentTimeMillis()),
                        new User("email", "password"),
                        1,
                        1,
                        1,
                        1,
                        Collections.emptyList());

        recipeController.addRecipe(recipeRequest, response);
        verify(response).setStatus(HttpStatus.CREATED.value());
    }
}