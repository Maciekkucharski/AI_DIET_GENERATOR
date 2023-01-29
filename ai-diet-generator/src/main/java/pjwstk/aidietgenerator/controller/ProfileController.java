package pjwstk.aidietgenerator.controller;

import com.google.api.Http;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pjwstk.aidietgenerator.entity.UserExtras;
import pjwstk.aidietgenerator.request.SearchRequest;
import pjwstk.aidietgenerator.request.UserExtrasRequest;
import pjwstk.aidietgenerator.view.*;
import org.springframework.web.bind.annotation.*;
import pjwstk.aidietgenerator.request.ProfileInfoRequest;
import pjwstk.aidietgenerator.service.ProfileService;


import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/account/profile")
@CrossOrigin(exposedHeaders = "*")
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
    public UserProfile getSelectedUserProfile(@PathVariable (value = "id") Long id,
                                              HttpServletResponse response){
        return profileService.getSelectedUserProfile(id, response);
    }

    @GetMapping("/info")
    public ProfileInfoView getCurrentUserProfileInfo(HttpServletResponse response){
        return profileService.getLoggedUserProfileInfo(response);
    }

    @PostMapping("/info")
    public void updateCurrentUserProfileInfo(@RequestBody ProfileInfoRequest profileInfoRequest,
                                             HttpServletResponse response){
        profileService.updateLoggedUserProfileInfo(profileInfoRequest, response);
    }

    @GetMapping("/info/weights")
    public List<WeightView> getCurrentUserWeightHistory(HttpServletResponse response){
        return profileService.getCurrentUsersWeightStory(response);
    }
    @DeleteMapping("/info/weights")
    public void deleteUserStatsWeightEntry(@RequestBody List<Long> ids,
                                           HttpServletResponse response){
        profileService.deleteUserStatsWeightEntry(ids, response);
    }

    @GetMapping("/extras")
    public UserExtras getUserExtras(HttpServletResponse response){
        return profileService.currentUserExtras(response);
    }

    @GetMapping("/extras/{userID}")
    public UserExtras getUserExtras(@PathVariable (value = "userID") Long userID,
                                    HttpServletResponse response){
        return profileService.givenUserExtras(userID, response);
    }

    @PostMapping("/extras")
    public UserExtras createUserExtras(@RequestBody UserExtrasRequest userExtrasRequest,
                                       HttpServletResponse response){
        return profileService.saveUserExtras(userExtrasRequest, response);
    }

    @PutMapping("/extras")
    public UserExtras updateExtras(@RequestBody UserExtrasRequest userExtrasRequest,
                                   HttpServletResponse response){
        return profileService.updateUserExtras(userExtrasRequest, response);
    }

    @DeleteMapping("/extras")
    public void deleteExtras(HttpServletResponse response){
        profileService.deleteUserExtras(response);
    }

    @PostMapping("/search")
    public List<ProfileSearchView> search(@RequestBody SearchRequest request,
                                          HttpServletResponse response){
        return profileService.searchUser(request, response);
    }
}
