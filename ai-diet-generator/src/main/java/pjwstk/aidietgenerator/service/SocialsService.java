package pjwstk.aidietgenerator.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.entity.Socials;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.repository.SocialsRepository;
import pjwstk.aidietgenerator.repository.UserRepository;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
public class SocialsService {

    private final UserService userService;
    private final SocialsRepository socialsRepository;
    private final UserRepository userRepository;

    public SocialsService(UserService userService, SocialsRepository socialsRepository, UserRepository userRepository) {
        this.userService = userService;
        this.socialsRepository = socialsRepository;
        this.userRepository = userRepository;
    }



    public Socials saveSocials(HttpServletResponse response, Socials socials){
        User currentUser = userService.findCurrentUser();
        if (currentUser == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        } else {
            Socials existingSocials = socialsRepository.findByuser(currentUser);
            if(existingSocials == null){
                Socials newSocials = new Socials();
                newSocials.setDiscord(socials.getDiscord());
                newSocials.setFacebook(socials.getFacebook());
                newSocials.setInstagram(socials.getInstagram());
                newSocials.setTelegram(socials.getTelegram());
                newSocials.setTwitter(socials.getTwitter());
                newSocials.setYoutube(socials.getYoutube());
                newSocials.setUser(currentUser);
                socialsRepository.save(newSocials);
                response.setStatus(HttpStatus.CREATED.value());
                return newSocials;
            } else {
                if(socials.getDiscord() != null)
                    existingSocials.setDiscord(socials.getDiscord());
                if(socials.getFacebook() != null)
                    existingSocials.setFacebook(socials.getFacebook());
                if(socials.getInstagram() != null)
                    existingSocials.setInstagram(socials.getInstagram());
                if(socials.getTelegram() != null)
                    existingSocials.setTelegram(socials.getTelegram());
                if(socials.getTwitter() != null)
                    existingSocials.setTwitter(socials.getTwitter());
                if(socials.getYoutube() != null)
                    existingSocials.setYoutube(socials.getYoutube());
                return socialsRepository.save(existingSocials);
            }
        }
    }

    public Socials getUserSocials(HttpServletResponse response, long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        } else {
            Socials socials = socialsRepository.findByuser(user.get());
            return socials;
        }
    }
}
