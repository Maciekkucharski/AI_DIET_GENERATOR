package pjwstk.aidietgenerator.controller;

import org.springframework.web.bind.annotation.*;
import pjwstk.aidietgenerator.entity.MyProfile;
import pjwstk.aidietgenerator.entity.UserProfile;
import pjwstk.aidietgenerator.request.ProfileInfoRequest;
import pjwstk.aidietgenerator.service.ProfileService;
import pjwstk.aidietgenerator.view.ProfileInfoView;


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

    @GetMapping("/info")
    public ProfileInfoView getCurrentUserProfileInfo(HttpServletResponse response){
        return profileService.getLoggedUserProfileInfo(response);
    }

    @PutMapping("/info")
    public void updateCurrentUserProfileInfo(@RequestBody ProfileInfoRequest profileInfoRequest, HttpServletResponse response){
        profileService.updateLoggedUserProfileInfo(profileInfoRequest, response);
    }

    @DeleteMapping("/info")
    public void deleteLastUserStatsEntry(HttpServletResponse response){
        profileService.deleteLastUserStatsEntry(response);
    }
}
