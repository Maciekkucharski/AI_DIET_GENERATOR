package pjwstk.aidietgenerator.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pjwstk.aidietgenerator.entity.Socials;
import pjwstk.aidietgenerator.repository.SocialsRepository;
import pjwstk.aidietgenerator.service.SocialsService;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@RestController
@RequestMapping("/account/socials")
public class SocialsController {

    SocialsService socialsService;

    public SocialsController(SocialsService socialsService) {
        this.socialsService = socialsService;
    }

    @PostMapping
    @Transactional
    public Socials addUserSocials(@RequestBody Socials socials, HttpServletResponse response){
        return this.socialsService.saveSocials(response, socials);
    }
}
