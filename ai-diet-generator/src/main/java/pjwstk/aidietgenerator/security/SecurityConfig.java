package pjwstk.aidietgenerator.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pjwstk.aidietgenerator.service.UserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private UserDetailsService jwtUserDetailsService;
    private JwtRequestFilter jwtRequestFilter;
    @Autowired
    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          UserDetailsService jwtUserDetailsService,
                          JwtRequestFilter jwtRequestFilter){
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests().antMatchers("/authenticate").permitAll()
                .anyRequest().authenticated().and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//                        .antMatchers(HttpMethod.GET, "/api/**").hasRole("ADMIN")
//                        .antMatchers(HttpMethod.GET, "/account/socials/**").permitAll()
//                        .antMatchers(HttpMethod.PUT, "/account/socials/").hasRole("USER")
//                        .antMatchers("/account/subscription").permitAll()
//                        .antMatchers(HttpMethod.POST, "/account/socials/").hasRole("USER")
//                        .antMatchers( "/userStats").hasRole("USER")
//                        .anyRequest().authenticated()
//                .oauth2Login()
//                .defaultSuccessUrl("/account/loginSuccess")
//                .failureUrl("/account/loginFailure")
//                .authorizationEndpoint()
//                .baseUri("/oauth2/authorize-client")
//                .authorizationRequestRepository(authorizationRequestRepository());
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }


//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().antMatchers();
//    }
}
