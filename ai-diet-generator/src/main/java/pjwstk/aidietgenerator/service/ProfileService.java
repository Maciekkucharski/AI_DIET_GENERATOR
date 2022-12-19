package pjwstk.aidietgenerator.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.entity.MyProfile;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.entity.UserProfile;
import pjwstk.aidietgenerator.entity.UserStats;
import pjwstk.aidietgenerator.repository.PostRepository;
import pjwstk.aidietgenerator.repository.SocialsRepository;
import pjwstk.aidietgenerator.repository.UserRepository;
import pjwstk.aidietgenerator.repository.UserStatsRepository;
import pjwstk.aidietgenerator.request.ProfileInfoRequest;
import pjwstk.aidietgenerator.view.ProfileInfoView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final SocialsRepository socialsRepository;
    private final UserStatsRepository userStatsRepository;
    private final UserDetailsService userDetailsService;
    private final PostRepository postRepository;

    private final UserStatsService userStatsService;

    public ProfileService(UserRepository userRepository,
                          SocialsRepository socialsRepository,
                          UserStatsRepository userStatsRepository,
                          UserDetailsService userDetailsService,
                          PostRepository postRepository,
                          UserStatsService userStatsService) {
        this.userRepository = userRepository;
        this.socialsRepository = socialsRepository;
        this.userStatsRepository = userStatsRepository;
        this.userDetailsService = userDetailsService;
        this.postRepository = postRepository;
        this.userStatsService = userStatsService;
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

    public ProfileInfoView getLoggedUserProfileInfo(HttpServletResponse response){
        User currentUser = userDetailsService.findCurrentUser();
        List<UserStats> currentUserStats = userStatsRepository.findByuser(currentUser);
        UserStats lastUserStats = currentUserStats.get(currentUserStats.size() - 1);
        if(currentUser != null){
            response.setStatus(HttpStatus.OK.value());
            return new ProfileInfoView(currentUser.getId(),
//                    TODO
                    "TODO",
                    currentUser.getFirstName(),
                    currentUser.getLastName(),
                    currentUser.getEmail(),
                    lastUserStats.getWeight(),
                    lastUserStats.getHeight(),
                    lastUserStats.getGender(),
                    lastUserStats.getBmi(),
//                    TODO
                    2200);
        }
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return null;
    }

    public void updateLoggedUserProfileInfo(ProfileInfoRequest profileInfoRequest, HttpServletResponse response) {
        User currentUser = userDetailsService.findCurrentUser();
        if(currentUser == null){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            UserStats updatedUserStats = new UserStats();

            currentUser.setFirstName(profileInfoRequest.getFirstName());
            currentUser.setLastName(profileInfoRequest.getLastName());
            currentUser.setEmail(profileInfoRequest.getEmail());

            updatedUserStats.setWeight(profileInfoRequest.getWeight());
            updatedUserStats.setHeight(profileInfoRequest.getHeight());
            updatedUserStats.setGender(profileInfoRequest.getGender());
            updatedUserStats.setBmi(userStatsService.calculateBmi(profileInfoRequest.getWeight(), profileInfoRequest.getHeight()));
            updatedUserStats.setUpdatedAt();
            updatedUserStats.setUser(currentUser);

            response.setStatus(HttpStatus.OK.value());
            userRepository.save(currentUser);
            userStatsRepository.save(updatedUserStats);
        }
    }

    public void deleteLastUserStatsEntry(HttpServletResponse response){
        User currentUser = userDetailsService.findCurrentUser();
        if(currentUser == null){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            List<UserStats> currentUserStats = userStatsRepository.findByuser(currentUser);
            int last_index = currentUserStats.size() - 1;
            userStatsRepository.delete(currentUserStats.get(last_index));
            response.setStatus(HttpStatus.ACCEPTED.value());
        }
    }
}
