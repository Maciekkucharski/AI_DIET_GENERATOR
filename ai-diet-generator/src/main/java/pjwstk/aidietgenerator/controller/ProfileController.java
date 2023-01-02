package pjwstk.aidietgenerator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pjwstk.aidietgenerator.view.MyProfile;
import pjwstk.aidietgenerator.view.UserProfile;
import org.springframework.web.bind.annotation.*;
import pjwstk.aidietgenerator.request.ProfileInfoRequest;
import pjwstk.aidietgenerator.service.ProfileService;
import pjwstk.aidietgenerator.view.ProfileInfoView;
import pjwstk.aidietgenerator.view.WeightView;


import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    @PostMapping("/info")
    public void updateCurrentUserProfileInfo(@RequestBody ProfileInfoRequest profileInfoRequest, HttpServletResponse response){
        profileService.updateLoggedUserProfileInfo(profileInfoRequest, response);
    }

    @GetMapping("/info/weights")
    public List<WeightView> getCurrentUserWeightHistory(HttpServletResponse response){
        return profileService.getCurrentUsersWeightStory(response);
    }
    @DeleteMapping("/info/weights/{id}")
    public void deleteUserStatsEntry(@PathVariable(value = "id") Long id, HttpServletResponse response){
        profileService.deleteUserStatsEntry(id, response);
    }
}
