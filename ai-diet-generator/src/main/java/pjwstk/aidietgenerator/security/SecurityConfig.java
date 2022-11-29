package pjwstk.aidietgenerator.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig{

    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest>
            authorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers(HttpMethod.POST, "/account/login", "/account/register").permitAll()
                        .antMatchers(HttpMethod.GET, "/api/**").hasRole("ADMIN")
                        .antMatchers(HttpMethod.GET, "/account/socials/**").permitAll()
                        .antMatchers(HttpMethod.PUT, "/account/socials/").hasRole("USER")
                        .antMatchers("/account/subscription").permitAll()
                        .antMatchers(HttpMethod.POST, "/account/socials/").hasRole("USER")
                        .antMatchers( "/userDetails").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .csrf().disable()
                .oauth2Login()
                .defaultSuccessUrl("/account/loginSuccess")
                .failureUrl("/account/loginFailure")
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize-client")
                .authorizationRequestRepository(authorizationRequestRepository());
        return http.build();
    }


//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().antMatchers();
//    }
}
