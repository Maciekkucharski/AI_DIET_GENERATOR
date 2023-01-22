package pjwstk.aidietgenerator.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.request.JwtRequest;
import pjwstk.aidietgenerator.security.Utils.JwtTokenUtil;
import pjwstk.aidietgenerator.service.UserDetailsService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JwtAuthenticationControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserDetailsService jwtInMemoryUserDetailsService;

    @InjectMocks
    private JwtAuthenticationController jwtAuthenticationController;

    @Test
    @DisplayName("Should throw an exception when the email is null")
    void generateAuthenticationTokenWhenEmailIsNullThenThrowException() {
        JwtRequest jwtRequest = new JwtRequest(null, "password");
        assertThrows(
                Exception.class,
                () -> jwtAuthenticationController.generateAuthenticationToken(jwtRequest));
    }

    @Test
    @DisplayName("Should throw an exception when the password is null")
    void generateAuthenticationTokenWhenPasswordIsNullThenThrowException() {
        JwtRequest jwtRequest = new JwtRequest("email", null);
        assertThrows(
                Exception.class,
                () -> jwtAuthenticationController.generateAuthenticationToken(jwtRequest));
    }

    @Test
    @DisplayName("Should return a token when the credentials are valid")
    void generateAuthenticationTokenWhenCredentialsAreValidThenReturnAToken() throws Exception {
        JwtRequest authenticationRequest = new JwtRequest("test@test.com", "test");
        User user = new User("test@test.com", "test", "ROLE_USER");
        String token = "token";

        when(jwtInMemoryUserDetailsService.findByEmail(authenticationRequest.getEmail()))
                .thenReturn(user);
        when(jwtTokenUtil.generateToken(user)).thenReturn(token);

        assertEquals(
                token,
                jwtAuthenticationController
                        .generateAuthenticationToken(authenticationRequest)
                        .getBody().toString());
    }

    @Test
    @DisplayName("Should throw an exception when the credentials are invalid")
    void generateAuthenticationTokenWhenCredentialsAreInvalidThenThrowException() {
        JwtRequest jwtRequest = new JwtRequest("email", "password");
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException(""));

        Exception exception =
                assertThrows(
                        Exception.class,
                        () -> jwtAuthenticationController.generateAuthenticationToken(jwtRequest));

        assertEquals("INVALID_CREDENTIALS", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw an exception when the user is disabled")
    void generateAuthenticationTokenWhenUserIsDisabledThenThrowException() {
        JwtRequest jwtRequest = new JwtRequest("test@test.com", "password");
        User user = new User("test@test.com", "password");
        String token = "token";
        when(jwtInMemoryUserDetailsService.findByEmail(jwtRequest.getEmail())).thenReturn(user);
        when(jwtTokenUtil.generateToken(user)).thenReturn(token);
        doThrow(new DisabledException("USER_DISABLED"))
                .when(authenticationManager)
                .authenticate(any());

        Exception exception =
                assertThrows(
                        Exception.class,
                        () -> jwtAuthenticationController.generateAuthenticationToken(jwtRequest));

        assertEquals("USER_DISABLED", exception.getMessage());
    }
}