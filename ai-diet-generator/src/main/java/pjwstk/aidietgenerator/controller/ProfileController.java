package pjwstk.aidietgenerator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pjwstk.aidietgenerator.entity.Profile;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.repository.SocialsRepository;
import pjwstk.aidietgenerator.repository.UserRepository;
import pjwstk.aidietgenerator.repository.UserStatsRepository;
import pjwstk.aidietgenerator.service.UserDetailsService;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/account/profile")
public class ProfileController {

    private final UserRepository userRepository;
    private final SocialsRepository socialsRepository;
    private final UserStatsRepository userStatsRepository;
    private final UserDetailsService userDetailsService;

    public ProfileController(UserRepository userRepository,
                             SocialsRepository socialsRepository,
                             UserStatsRepository userStatsRepository,
                             UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.socialsRepository = socialsRepository;
        this.userStatsRepository = userStatsRepository;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping
    public Profile getCurrentUserProfile(HttpServletResponse response){
        Profile currentUserProfile = new Profile();
        User currentUser = userDetailsService.findCurrentUser();
        if(currentUser != null){
            currentUserProfile.setUser(currentUser);
            currentUserProfile.setProfilePicturePath("TODO");
            currentUserProfile.setUserStats(userStatsRepository.findByuser(currentUser));
            currentUserProfile.setSocials(socialsRepository.findByuser(currentUser));
            response.setStatus(HttpStatus.OK.value());
            return currentUserProfile;
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        }

    }
}
