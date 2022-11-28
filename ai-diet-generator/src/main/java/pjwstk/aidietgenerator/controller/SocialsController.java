package pjwstk.aidietgenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pjwstk.aidietgenerator.entity.Socials;
import pjwstk.aidietgenerator.repository.SocialsRepository;
import pjwstk.aidietgenerator.service.SocialsService;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@RestController
@RequestMapping("/account/socials")
public class SocialsController {

    SocialsService socialsService;

    @Autowired
    public SocialsController(SocialsService socialsService) {
        this.socialsService = socialsService;
    }

    @GetMapping("/{id}")
    public Socials getUserSocials(@PathVariable(value = "id" ) long userId, HttpServletResponse response) {
        return socialsService.getUserSocials(response, userId);
    }

    @PostMapping
    @Transactional
    public Socials addUserSocials(@RequestBody Socials socials, HttpServletResponse response){
        return socialsService.saveSocials(response, socials);
    }

    @PutMapping
    @Transactional
    public Socials editUserSocials(@RequestBody Socials socials, HttpServletResponse response){
        return socialsService.updateSocials(response, socials);
    }
}
