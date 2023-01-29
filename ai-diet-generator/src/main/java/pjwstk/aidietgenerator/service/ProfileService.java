package pjwstk.aidietgenerator.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.repository.*;
import pjwstk.aidietgenerator.view.MyProfile;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.view.UserProfile;
import pjwstk.aidietgenerator.entity.UserStats;
import pjwstk.aidietgenerator.entity.*;
import pjwstk.aidietgenerator.request.SearchRequest;
import pjwstk.aidietgenerator.request.UserExtrasRequest;
import pjwstk.aidietgenerator.view.*;
import pjwstk.aidietgenerator.request.ProfileInfoRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final UserStatsRepository userStatsRepository;
    private final UserDetailsService userDetailsService;
    private final PostRepository postRepository;
    private final UserStatsService userStatsService;
    private final WeekDietRepository weekDietRepository;
    private final UserExtrasRepository userExtrasRepository;
    private final RecipeRepository recipeRepository;
    private final ExcludedProductsListRepository excludedProductsListRepository;
    private final ForumService forumService;
    private final FollowRepository followRepository;
    private final SubscriptionRepository subscriptionRepository;

    public ProfileService(UserRepository userRepository,
                          UserStatsRepository userStatsRepository,
                          UserDetailsService userDetailsService,
                          PostRepository postRepository,
                          UserStatsService userStatsService,
                          WeekDietRepository weekDietRepository,
                          UserExtrasRepository userExtrasRepository,
                          RecipeRepository recipeRepository,
                          ExcludedProductsListRepository excludedProductsListRepository,
                          ForumService forumService,
                          FollowRepository followRepository,
                          SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.userStatsRepository = userStatsRepository;
        this.userDetailsService = userDetailsService;
        this.postRepository = postRepository;
        this.userStatsService = userStatsService;
        this.weekDietRepository = weekDietRepository;
        this.userExtrasRepository = userExtrasRepository;
        this.recipeRepository = recipeRepository;
        this.excludedProductsListRepository = excludedProductsListRepository;
        this.forumService = forumService;
        this.followRepository = followRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public MyProfile getLoggedUserProfile(HttpServletResponse response){
        MyProfile currentUserProfile = new MyProfile();
        User currentUser = userDetailsService.findCurrentUser();
        if(currentUser != null) {
            currentUserProfile.setUser(currentUser);
            currentUserProfile.setProfileImagePath(currentUser.getImagePath());
            List<PostDetailedView> userPostsView = new ArrayList<>();
            for(Post post : postRepository.findByuser(currentUser)){
                userPostsView.add(forumService.viewPost(post.getId(), response));
            }
            currentUserProfile.setUserPosts(userPostsView);
            currentUserProfile.setProfileImagePath(currentUser.getImagePath());
            currentUserProfile.setUserSubscriptions(subscriptionRepository.findByUser(currentUser));
            currentUserProfile.setUserStats(userStatsRepository.findByuser(currentUser));
            List<UserStats> currentUserStats = userStatsRepository.findByuser(currentUser);
            if (currentUser != null && !currentUserStats.isEmpty()) {
                if(currentUserStats.get(currentUserStats.size() - 1).getCal() != null) {
                    currentUserProfile.setDailyCalGoal(currentUserStats.get(currentUserStats.size() - 1).getCal());
                } else {
                    currentUserProfile.setDailyCalGoal(0);
                }
            } else {
                currentUserProfile.setDailyCalGoal(0);
            }
            DietWeek userDietWeek = weekDietRepository.findByuser(currentUser);
            if (userDietWeek != null) {
                currentUserProfile.setDietGoal(userDietWeek.getDietGoal());
                currentUserProfile.setWeightAtDietGeneration(userDietWeek.getStartingWeight());
                currentUserProfile.setMealsPerDay(userDietWeek.getDaysForWeekDiet().get(0).getRecipesForToday().size());
            } else {
                currentUserProfile.setDietGoal(null);
                currentUserProfile.setWeightAtDietGeneration(0.0);
                currentUserProfile.setMealsPerDay(0);
            }
            currentUserProfile.setUserPosts(userPostsView);
            if(userDietWeek != null) {
                currentUserProfile.setDietGoal(userDietWeek.getDietGoal());
                currentUserProfile.setWeightAtDietGeneration(userDietWeek.getStartingWeight());
                currentUserProfile.setMealsPerDay(userDietWeek.getDaysForWeekDiet().get(0).getRecipesForToday().size());
            }
            currentUserProfile.setUserRecipes(recipeRepository.findByuser(currentUser));
            currentUserProfile.setExcludedProductsList(excludedProductsListRepository.findByuser(currentUser));
            UserExtras userExtras = userExtrasRepository.findByuser(currentUser);
            currentUserProfile.setUserExtras(userExtras);
            if(userExtras != null) {
                currentUserProfile.setSocials(new SocialsView(userExtras.getFacebook(),
                        userExtras.getTwitter(), userExtras.getInstagram(), userExtras.getTelegram(), userExtras.getYoutube(), userExtras.getDiscord()));
            } else {
                currentUserProfile.setSocials(null);
            }
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
            List<PostDetailedView> userPostsView = new ArrayList<>();
            for(Post post : postRepository.findByuser(selectedUser.get())){
                userPostsView.add(forumService.viewPost(post.getId(), response));
            }
            selectedUserProfile.setUser(selectedUser.get());
            if(userExtras != null) {
                selectedUserProfile.setUserExtras(userExtras);
                SocialsView userSocials = new SocialsView();
                userSocials.setFacebook(userExtras.getFacebook());
                userSocials.setDiscord(userExtras.getDiscord());
                userSocials.setInstagram(userExtras.getInstagram());
                userSocials.setTwitter(userExtras.getTwitter());
                userSocials.setYoutube(userExtras.getYoutube());
                userSocials.setTelegram(userExtras.getTelegram());
                selectedUserProfile.setSocials(userSocials);
            }
            selectedUserProfile.setFollowingList(userFollows);
            selectedUserProfile.setUserPosts(userPostsView);
            selectedUserProfile.setUserRecipes(recipeRepository.findByuser(selectedUser.get()));
            selectedUserProfile.setUserImagePath(selectedUser.get().getImagePath());
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
                            .collect(Collectors.toList()),
                    currentUser.getSurvey(),
                    currentUser.getRating(),
                    currentUser.getSubscribed()
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
                    if(profileInfoRequest.getWeight() != null || profileInfoRequest.getHeight() != null ||
                            profileInfoRequest.getAge() != null || profileInfoRequest.getGender() != null) {
                        userStatsRepository.save(updatedUserStats);
                    }
                    response.setStatus(HttpStatus.OK.value());
                } else {
                    if(profileInfoRequest.getEmail().equals(currentUser.getEmail())){
                        userRepository.save(currentUser);
                        if(profileInfoRequest.getWeight() != null || profileInfoRequest.getHeight() != null ||
                                profileInfoRequest.getAge() != null || profileInfoRequest.getGender() != null) {
                            userStatsRepository.save(updatedUserStats);
                        }
                        response.setStatus(HttpStatus.OK.value());
                    } else {
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                    }
                }
            } else {
                userRepository.save(currentUser);
                if(profileInfoRequest.getWeight() != null || profileInfoRequest.getHeight() != null ||
                        profileInfoRequest.getAge() != null || profileInfoRequest.getGender() != null) {
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
            response.setStatus(HttpStatus.OK.value());
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

    public UserExtras saveUserExtras(UserExtrasRequest userExtrasRequest, HttpServletResponse response) {
        User currentUser = userDetailsService.findCurrentUser();
        if(currentUser != null){
            UserExtras existingUserExtras = userExtrasRepository.findByuser(currentUser);
            if(existingUserExtras == null) {
                UserExtras newUserExtras = new UserExtras();
                newUserExtras.setUser(currentUser);
                newUserExtras.setBackgroundImagePath(userExtrasRequest.getBackgroundImagePath());
                newUserExtras.setProfession(userExtrasRequest.getProfession());
                newUserExtras.setAbout_me(userExtrasRequest.getAbout_me());
                newUserExtras.setDiscord(userExtrasRequest.getDiscord());
                newUserExtras.setFacebook(userExtrasRequest.getFacebook());
                newUserExtras.setInstagram(userExtrasRequest.getInstagram());
                newUserExtras.setTelegram(userExtrasRequest.getTelegram());
                newUserExtras.setTwitter(userExtrasRequest.getTwitter());
                newUserExtras.setYoutube(userExtrasRequest.getYoutube());
                response.setStatus(HttpStatus.OK.value());
                return userExtrasRepository.save(newUserExtras);

            } else {
                return updateUserExtras(userExtrasRequest, response);
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        }
    }

    public UserExtras updateUserExtras(UserExtrasRequest request, HttpServletResponse response){
        User currentUser = userDetailsService.findCurrentUser();
        if(currentUser != null){
            UserExtras existingExtras = userExtrasRepository.findByuser(currentUser);
            if(existingExtras != null) {
                if(request != null)
                    existingExtras.setBackgroundImagePath(request.getBackgroundImagePath());
                if(request.getProfession() != null)
                    existingExtras.setProfession(request.getProfession());
                if(request.getAbout_me() != null)
                    existingExtras.setAbout_me(request.getAbout_me());
                if(request.getDiscord() != null)
                    existingExtras.setDiscord(request.getDiscord());
                if(request.getFacebook() != null)
                    existingExtras.setFacebook(request.getFacebook());
                if(request.getInstagram() != null)
                    existingExtras.setInstagram(request.getInstagram());
                if(request.getTelegram() != null)
                    existingExtras.setTelegram(request.getTelegram());
                if(request.getTwitter() != null)
                    existingExtras.setTwitter(request.getTwitter());
                if(request.getYoutube() != null)
                    existingExtras.setYoutube(request.getYoutube());
                response.setStatus(HttpStatus.OK.value());
                return userExtrasRepository.save(existingExtras);
            } else {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return null;
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
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

    public List<ProfileSearchView> searchUser(SearchRequest request, HttpServletResponse response) {
        List<ProfileSearchView> profileSearchViewList = new ArrayList<>();
        if(request.getSearch() != null){
            List<User> firstSearch = new ArrayList<>();
            List<User> secondSearch = new ArrayList<>();
            String[] words = request.getSearch().split(" ");
            for(String word : words){
                word = word.replaceAll("\\s", "");
            }
            if(words.length == 2) {
                firstSearch = userRepository.findByFirstNameAndLastName(words[0], words[1]);
                secondSearch = userRepository.findByFirstNameAndLastName(words[1], words[0]);
            } else {
                firstSearch = userRepository.findByFirstNameOrLastName(words[0], words[0]);
                secondSearch = firstSearch;
            }
            Set<User> searchResult = new HashSet<>(firstSearch);
            searchResult.addAll(secondSearch);
            for(User user : searchResult){
                ProfileSearchView newSearchView = new ProfileSearchView();
                newSearchView.setID(user.getId());
                newSearchView.setFirstName(user.getFirstName());
                newSearchView.setLastName(user.getLastName());
                newSearchView.setImagePath(user.getImagePath());
                profileSearchViewList.add(newSearchView);
            }
            return profileSearchViewList;
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return null;
        }
    }

    public UserExtras currentUserExtras(HttpServletResponse response) {
        User currentUser = userDetailsService.findCurrentUser();
        if (currentUser != null) {
            UserExtras existingExtras = userExtrasRepository.findByuser(currentUser);
            if (existingExtras != null) {
                response.setStatus(HttpStatus.OK.value());
                return existingExtras;
            } else {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return null;
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        }
    }

    public UserExtras givenUserExtras(Long userID, HttpServletResponse response) {
        Optional<User> existingUser = userRepository.findById(userID);
        if (existingUser.isPresent()) {
            UserExtras existingExtras = userExtrasRepository.findByuser(existingUser.get());
            if (existingExtras != null) {
                response.setStatus(HttpStatus.OK.value());
                return existingExtras;
            } else {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return null;
            }
        } else {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        }
    }
}
