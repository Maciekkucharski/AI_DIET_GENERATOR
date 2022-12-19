package pjwstk.aidietgenerator.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.entity.MyProfile;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.entity.UserProfile;
import pjwstk.aidietgenerator.repository.PostRepository;
import pjwstk.aidietgenerator.repository.SocialsRepository;
import pjwstk.aidietgenerator.repository.UserRepository;
import pjwstk.aidietgenerator.repository.UserStatsRepository;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final SocialsRepository socialsRepository;
    private final UserStatsRepository userStatsRepository;
    private final UserDetailsService userDetailsService;
    private final PostRepository postRepository;

    public ProfileService(UserRepository userRepository,
                          SocialsRepository socialsRepository,
                          UserStatsRepository userStatsRepository,
                          UserDetailsService userDetailsService,
                          PostRepository postRepository) {
        this.userRepository = userRepository;
        this.socialsRepository = socialsRepository;
        this.userStatsRepository = userStatsRepository;
        this.userDetailsService = userDetailsService;
        this.postRepository = postRepository;
    }

    public MyProfile getLoggedUserProfile(HttpServletResponse response){
        MyProfile currentUserProfile = new MyProfile();
        User currentUser = userDetailsService.findCurrentUser();
        if(currentUser != null){
            currentUserProfile.setUser(currentUser);
            currentUserProfile.setProfilePicturePath("TODO");
            currentUserProfile.setUserStats(userStatsRepository.findByuser(currentUser));
            currentUserProfile.setSocials(socialsRepository.findByuser(currentUser));
            currentUserProfile.setUserPosts(postRepository.findByuser(currentUser));
            response.setStatus(HttpStatus.OK.value());
            return currentUserProfile;
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        }
    }

    public UserProfile getSelectedUserProfile(Long userID, HttpServletResponse response){
        UserProfile selectedUserProfile = new UserProfile();
        Optional<User> selectedUser = userRepository.findById(userID);
        if(!selectedUser.isEmpty()){
            selectedUserProfile.setUser(selectedUser.get());
            selectedUserProfile.setProfilePicturePath("TODO");
            selectedUserProfile.setSocials(socialsRepository.findByuser(selectedUser.get()));
            selectedUserProfile.setUserPosts(postRepository.findByuser(selectedUser.get()));
            response.setStatus(HttpStatus.OK.value());
            return selectedUserProfile;
        }else {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        }
    }
}
