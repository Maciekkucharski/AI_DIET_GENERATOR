package pjwstk.aidietgenerator.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.entity.Socials;
import pjwstk.aidietgenerator.entity.User;
import pjwstk.aidietgenerator.repository.SocialsRepository;

import javax.servlet.http.HttpServletResponse;

@Service
public class SocialsService {

    private final UserService userService;
    private final SocialsRepository socialsRepository;

    public SocialsService(UserService userService, SocialsRepository socialsRepository) {
        this.userService = userService;
        this.socialsRepository = socialsRepository;
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
                this.socialsRepository.save(newSocials);
                response.setStatus(HttpStatus.CREATED.value());
                return newSocials;
            } else {
                if(socials.getDiscord() != null) existingSocials.setDiscord(socials.getDiscord());
                if(socials.getFacebook() != null) existingSocials.setFacebook(socials.getFacebook());
                if(socials.getInstagram() != null) existingSocials.setInstagram(socials.getInstagram());
                if(socials.getTelegram() != null) existingSocials.setTelegram(socials.getTelegram());
                if(socials.getTwitter() != null) existingSocials.setTwitter(socials.getTwitter());
                if(socials.getYoutube() != null) existingSocials.setYoutube(socials.getYoutube());
                return this.socialsRepository.save(existingSocials);
            }
        }
    }
}
