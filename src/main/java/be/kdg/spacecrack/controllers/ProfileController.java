package be.kdg.spacecrack.controllers;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.jsonviewmodels.ProfileWrapper;
import be.kdg.spacecrack.model.Profile;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.TokenRepository;
import be.kdg.spacecrack.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/auth/profile")
public class ProfileController {
    @Autowired
    ProfileService contactService;
    @Autowired
    IUserService userService;
    @Autowired
    IAuthorizationService tokenService;

    public ProfileController() {

    }

    public ProfileController(ProfileService contactService, UserService userService, TokenRepository tokenRepository, IAuthorizationService tokenService){
        this.contactService = contactService;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public void createProfile(@RequestBody ProfileWrapper profileWrapper, @CookieValue("accessToken") String accessTokenValue) throws Exception {


        User user = tokenService.getUserByAccessTokenValue(accessTokenValue);

        Date date = new SimpleDateFormat("dd-mm-yyyy").parse(profileWrapper.getDayOfBirth());
        Profile profile = new Profile(profileWrapper.getFirstname(), profileWrapper.getLastname(), profileWrapper.getEmail(), date, profileWrapper.getImage());

        contactService.createProfile(profile, user);
    }

}
