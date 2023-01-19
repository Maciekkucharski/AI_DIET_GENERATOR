package pjwstk.aidietgenerator.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.entity.*;
import pjwstk.aidietgenerator.repository.*;
import pjwstk.aidietgenerator.request.UserExtrasRequest;
import pjwstk.aidietgenerator.view.*;
import pjwstk.aidietgenerator.request.ProfileInfoRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final SocialsRepository socialsRepository;
    private final UserStatsRepository userStatsRepository;
    private final UserDetailsService userDetailsService;
    private final PostRepository postRepository;
    private final UserStatsService userStatsService;
    private final UserExtrasRepository userExtrasRepository;
    private final RecipeRepository recipeRepository;
    private final ExcludedProductsListRepository excludedProductsListRepository;
    private final ForumService forumService;
    private final FollowRepository followRepository;

    public ProfileService(UserRepository userRepository,
                          SocialsRepository socialsRepository,
                          UserStatsRepository userStatsRepository,
                          UserDetailsService userDetailsService,
                          PostRepository postRepository,
                          UserStatsService userStatsService,
                          UserExtrasRepository userExtrasRepository,
                          RecipeRepository recipeRepository,
                          ExcludedProductsListRepository excludedProductsListRepository,
                          ForumService forumService,
                          FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.socialsRepository = socialsRepository;
        this.userStatsRepository = userStatsRepository;
        this.userDetailsService = userDetailsService;
        this.postRepository = postRepository;
        this.userStatsService = userStatsService;
        this.userExtrasRepository = userExtrasRepository;
        this.recipeRepository = recipeRepository;
        this.excludedProductsListRepository = excludedProductsListRepository;
        this.forumService = forumService;
        this.followRepository = followRepository;
    }

    public MyProfile getLoggedUserProfile(HttpServletResponse response){
        MyProfile currentUserProfile = new MyProfile();
        User currentUser = userDetailsService.findCurrentUser();
        if(currentUser != null){
            currentUserProfile.setUser(currentUser);
            currentUserProfile.setProfileImagePath(currentUser.getImagePath());
            currentUserProfile.setSocials(socialsRepository.findByuser(currentUser));
            List<PostDetailedView> userPostsView = new ArrayList<>();
            for(Post post : postRepository.findByuser(currentUser)){
                userPostsView.add(forumService.viewPost(post.getId(), response));
            }
            currentUserProfile.setUserPosts(userPostsView);
            currentUserProfile.setProfileImagePath(currentUser.getImagePath());
            currentUserProfile.setSubscribed(true); // TODO
            currentUserProfile.setUserStats(userStatsRepository.findByuser(currentUser));
            currentUserProfile.setUserRecipes(recipeRepository.findByuser(currentUser));
            currentUserProfile.setExcludedProductsList(excludedProductsListRepository.findByuser(currentUser));
            currentUserProfile.setUserExtras(userExtrasRepository.findByuser(currentUser));
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
        if(selectedUser.isPresent()){
            UserExtras userExtras = userExtrasRepository.findByuser(selectedUser.get());
            List<Follow> userFollows = followRepository.findByUser(selectedUser.get());
            Socials userSocials = socialsRepository.findByuser(selectedUser.get());
            List<PostDetailedView> userPostsView = new ArrayList<>();
            for(Post post : postRepository.findByuser(selectedUser.get())){
                userPostsView.add(forumService.viewPost(post.getId(), response));
            }
            selectedUserProfile.setUser(selectedUser.get());
            selectedUserProfile.setUserExtras(userExtras);
            selectedUserProfile.setFollowerCount(userFollows.size());
            selectedUserProfile.setSocials(userSocials);
            selectedUserProfile.setSocials(socialsRepository.findByuser(selectedUser.get()));
            selectedUserProfile.setUserPosts(userPostsView);
            selectedUserProfile.setUserRecipes(recipeRepository.findByuser(selectedUser.get()));
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
        UserStats lastUserStats = new UserStats();
        if(!currentUserStats.isEmpty()){
            lastUserStats = currentUserStats.get(currentUserStats.size() - 1);
        } else {
            lastUserStats.setAge(0);
            lastUserStats.setWeight(0.0);
            lastUserStats.setHeight(0);
            lastUserStats.setGender(null);
            lastUserStats.setBmi(0.0);
            lastUserStats.setCal(0);
        }
        if(currentUser != null){
            response.setStatus(HttpStatus.OK.value());
            return new ProfileInfoView(currentUser.getId(),
                    currentUser.getImagePath(),
                    currentUser.getFirstName(),
                    currentUser.getLastName(),
                    currentUser.getEmail(),
                    lastUserStats.getAge(),
                    lastUserStats.getWeight(),
                    lastUserStats.getHeight(),
                    lastUserStats.getGender(),
                    lastUserStats.getBmi(),
                    lastUserStats.getCal() == null ? 0 : lastUserStats.getCal(),
                    currentUser.getAuthorities().stream()
                            .map(authorities -> authorities.toString())
                            .map(String::trim)
                            .filter(authority -> !authority.equals(""))
                            .collect(Collectors.toList())
                  );
        }
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return null;
    }

    public void updateLoggedUserProfileInfo(ProfileInfoRequest profileInfoRequest, HttpServletResponse response) {
        User currentUser = userDetailsService.findCurrentUser();
        if(currentUser == null){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            List<UserStats> currentUserStats = userStatsRepository.findByuser(currentUser);
            UserStats updatedUserStats = new UserStats();

            Integer latestHeight = 0;
            Double latestWeight = 0.0;
            UserStats lastUserStats = new UserStats();

            if(!currentUserStats.isEmpty()) {
                lastUserStats = userStatsRepository.findByuser(currentUser).get(currentUserStats.size() - 1);
                latestHeight = lastUserStats.getHeight();
                latestWeight = lastUserStats.getWeight();
            } else {
                lastUserStats.setAge(0);
                lastUserStats.setWeight(0.0);
                lastUserStats.setHeight(0);
                lastUserStats.setGender(null);
                lastUserStats.setBmi(0.0);
                lastUserStats.setCal(0);
            }

            if(profileInfoRequest.getProfileImagePath() != null) {
                currentUser.setImagePath(profileInfoRequest.getProfileImagePath());
            }

            if(profileInfoRequest.getFirstName() != null) {currentUser.setFirstName(profileInfoRequest.getFirstName());}
            if(profileInfoRequest.getLastName() != null) {currentUser.setLastName(profileInfoRequest.getLastName());}

            if(profileInfoRequest.getAge() != null) {updatedUserStats.setAge(profileInfoRequest.getAge());
            } else {
                updatedUserStats.setAge(lastUserStats.getAge());
            }

            if(profileInfoRequest.getWeight() != null) {
                updatedUserStats.setWeight(profileInfoRequest.getWeight());
                latestWeight = profileInfoRequest.getWeight();
            } else {
                updatedUserStats.setWeight(latestWeight);
            }

            if(profileInfoRequest.getHeight() != null) {
                updatedUserStats.setHeight(profileInfoRequest.getHeight());
                latestHeight = profileInfoRequest.getHeight();
            } else {
                updatedUserStats.setHeight(latestHeight);
            }

            if(profileInfoRequest.getGender() != null) {updatedUserStats.setGender(profileInfoRequest.getGender());
            } else {
                updatedUserStats.setGender(lastUserStats.getGender());
            }

            if(latestWeight!=0 && latestHeight!=0) {
                updatedUserStats.setBmi(userStatsService.calculateBmi(latestWeight, latestHeight));
            } else {
                updatedUserStats.setBmi(0.0);
            }

            if(profileInfoRequest.getTimestamp() != null){
                updatedUserStats.setTimestamp(profileInfoRequest.getTimestamp());
            } else {
                updatedUserStats.setUpdatedAt();
            }
            updatedUserStats.setUser(currentUser);

            if(profileInfoRequest.getEmail() != null) {
                if(userRepository.findByemail(profileInfoRequest.getEmail()) == null) {
                    currentUser.setEmail(profileInfoRequest.getEmail());
                    userRepository.save(currentUser);
                    if(profileInfoRequest.getWeight() != null || profileInfoRequest.getHeight() != null || profileInfoRequest.getAge() != null || profileInfoRequest.getGender() != null) {
                        userStatsRepository.save(updatedUserStats);
                    }
                    response.setStatus(HttpStatus.OK.value());
                } else {
                    if(profileInfoRequest.getEmail().equals(currentUser.getEmail())){
                        userRepository.save(currentUser);
                        if(profileInfoRequest.getWeight() != null || profileInfoRequest.getHeight() != null || profileInfoRequest.getAge() != null || profileInfoRequest.getGender() != null) {
                            userStatsRepository.save(updatedUserStats);
                        }
                        response.setStatus(HttpStatus.OK.value());
                    } else {
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                    }
                }
            } else {
                userRepository.save(currentUser);
                if(profileInfoRequest.getWeight() != null || profileInfoRequest.getHeight() != null || profileInfoRequest.getAge() != null || profileInfoRequest.getGender() != null) {
                    userStatsRepository.save(updatedUserStats);
                }
                response.setStatus(HttpStatus.OK.value());
            }
        }
    }

    public List<WeightView> getCurrentUsersWeightStory(HttpServletResponse response){
        User currentUser = userDetailsService.findCurrentUser();
        if(currentUser == null){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        } else {
            List<UserStats> allUserStats = userStatsRepository.findByuser(currentUser);
            List<WeightView> weightHistory = new ArrayList<>();

            for(UserStats userStat : allUserStats){
                WeightView weightEntry = new WeightView(userStat.getId(),
                        userStat.getWeight(),
                        userStat.getTimestamp());

                weightHistory.add(weightEntry);
            }
            return weightHistory;
        }
    }

    public void deleteUserStatsWeightEntry(List<Long> ids, HttpServletResponse response){
        User currentUser = userDetailsService.findCurrentUser();
        if(currentUser == null){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            List<UserStats> userStatsForDay = userStatsRepository.findAllById(ids);
            if(userStatsForDay.isEmpty()){
                response.setStatus(HttpStatus.NOT_FOUND.value());
            } else {
                for (UserStats stats : userStatsForDay) {
                    stats.setWeight(0.0);
                    userStatsRepository.save(stats);
                }
                response.setStatus(HttpStatus.ACCEPTED.value());
            }
        }
    }

    public void saveUserExtras(UserExtrasRequest userExtrasRequest, HttpServletResponse response) {
        User currentUser = userDetailsService.findCurrentUser();
        if(currentUser != null){
            UserExtras newUserExtras = new UserExtras();
            newUserExtras.setUser(currentUser);
            newUserExtras.setBackground_image(userExtrasRequest.getBackgroundImagePath());
            newUserExtras.setProfession(userExtrasRequest.getProfession());
            newUserExtras.setAbout_me(userExtrasRequest.getAbout_me());
            userExtrasRepository.save(newUserExtras);
            response.setStatus(HttpStatus.OK.value());
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    public void updateUserExtras(UserExtrasRequest userExtrasRequest, HttpServletResponse response){
        User currentUser = userDetailsService.findCurrentUser();
        if(currentUser != null){
            UserExtras existingExtras = userExtrasRepository.findByuser(currentUser);
            if(existingExtras != null) {
                if(userExtrasRequest.getBackgroundImagePath().length()>0 && userExtrasRequest.getBackgroundImagePath().contains("www"))
                    existingExtras.setBackground_image(userExtrasRequest.getBackgroundImagePath());
                if(userExtrasRequest.getProfession().length()>0)
                    existingExtras.setProfession(userExtrasRequest.getProfession());
                if(userExtrasRequest.getAbout_me().length()>0)
                    existingExtras.setAbout_me(userExtrasRequest.getAbout_me());
                userExtrasRepository.save(existingExtras);
                response.setStatus(HttpStatus.OK.value());
            } else {
                response.setStatus(HttpStatus.NOT_FOUND.value());
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    public void deleteUserExtras(HttpServletResponse response){
        User currentUser = userDetailsService.findCurrentUser();
        if(currentUser != null){
            UserExtras existingExtras = userExtrasRepository.findByuser(currentUser);
            if(existingExtras != null) {
                userExtrasRepository.delete(existingExtras);
                response.setStatus(HttpStatus.OK.value());
            } else {
                response.setStatus(HttpStatus.NOT_FOUND.value());
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }
}
