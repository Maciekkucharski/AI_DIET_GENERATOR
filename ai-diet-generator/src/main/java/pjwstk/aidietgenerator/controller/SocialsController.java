package pjwstk.aidietgenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pjwstk.aidietgenerator.entity.Socials;
import pjwstk.aidietgenerator.repository.SocialsRepository;
import pjwstk.aidietgenerator.service.SocialsService;
import pjwstk.aidietgenerator.service.UserDetailsService;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@RestController
@RequestMapping("/account/socials")
public class SocialsController {

    private final SocialsService socialsService;
    private final SocialsRepository socialsRepository;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SocialsController(SocialsService socialsService,
                             SocialsRepository socialsRepository,
                             UserDetailsService userDetailsService) {
        this.socialsService = socialsService;
        this.socialsRepository = socialsRepository;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping
    public Socials getCurrentUserSocials(){
        return socialsRepository.findByuser(userDetailsService.findCurrentUser());
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
