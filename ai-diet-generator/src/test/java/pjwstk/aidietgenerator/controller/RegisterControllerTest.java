package pjwstk.aidietgenerator.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.repository.UserRepository;
import pjwstk.aidietgenerator.request.RegisterRequest;
import pjwstk.aidietgenerator.service.UserDetailsService;

import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RegisterControllerTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private RegisterController registerController;

    @Test
    @DisplayName("Should return status code 409 when the password is invalid")
    void registerWhenPasswordIsInvalidThenReturnStatusCode409() {
        RegisterRequest registerRequest = new RegisterRequest("test@test.com", "12345");
        registerController.register(registerRequest, response);
        verify(response, times(1)).setStatus(HttpStatus.CONFLICT.value());
    }

    @Test
    @DisplayName("Should return status code 409 when the email is already taken")
    void registerWhenEmailIsAlreadyTakenThenReturnStatusCode409() {
        RegisterRequest registerRequest = new RegisterRequest("test@test.com", "test");
        when(userDetailsService.doesUserExist(registerRequest.getEmail())).thenReturn(true);
        registerController.register(registerRequest, response);
        verify(response).setStatus(HttpStatus.CONFLICT.value());
    }

    @Test
    @DisplayName("Should return status code 201 when the email is not taken and password is valid")
    void registerWhenEmailIsNotTakenAndPasswordIsValidThenReturnStatusCode201() {
        RegisterRequest registerRequest = new RegisterRequest("test@test.com", "password");
        User user = new User("test@test.com", "password");
        when(userDetailsService.doesUserExist(registerRequest.getEmail())).thenReturn(false);
        when(userDetailsService.isEmpty()).thenReturn(true);
        when(userDetailsService.patternMatches(registerRequest.getEmail())).thenReturn(true);
        when(userDetailsService.saveUser(user)).thenReturn(user);
        registerController.register(registerRequest, response);
        verify(response, times(1)).setStatus(HttpStatus.CREATED.value());
    }
}