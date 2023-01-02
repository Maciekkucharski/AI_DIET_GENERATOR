package pjwstk.aidietgenerator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pjwstk.aidietgenerator.view.MyProfile;
import pjwstk.aidietgenerator.view.UserProfile;
import pjwstk.aidietgenerator.service.ProfileService;


import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/account/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public MyProfile getCurrentUserProfile(HttpServletResponse response){
        return profileService.getLoggedUserProfile(response);
    }

    @GetMapping("/{id}")
    public UserProfile getSelectedUserProfile(@PathVariable (value = "id") Long id, HttpServletResponse response){
        return profileService.getSelectedUserProfile(id, response);
    }
}
