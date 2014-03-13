package be.kdg.spacecrack.controllers;

import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.services.IAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;


/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
@Controller
public class TokenController{


    @Autowired
    private IAuthorizationService tokenService;


    public TokenController() {

    }

    public TokenController(IAuthorizationService tokenService) {
        this.tokenService = tokenService;
    }


    @PostConstruct
    public void createTestUsers() {

        String hashedPw = tokenService.getMD5HashedPassword("test");
        tokenService.createTestUser("test@gmail.com", "test", hashedPw, "jack", "black");
        tokenService.createTestUser("opponentje@gmail.com", "OpponentTest", hashedPw,"speedy","gonzales");
    }

    @RequestMapping(value="/accesstokens", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody AccessToken login(@RequestBody User user) {
       // tokenService.createTestUsers();
        AccessToken accessToken = tokenService.login(user);

        return accessToken;
    }

    @RequestMapping(value="/accesstokens", method = RequestMethod.DELETE)
    @ResponseBody
    public void Logout(@CookieValue("accessToken") String accessTokenValue) throws Exception {
        String substring = accessTokenValue.substring(1, accessTokenValue.length() - 1);
        tokenService.logout(substring);
    }

    @RequestMapping(value="/auth/accesstokens", method = RequestMethod.GET)
    @ResponseBody
    public void checkUserLoggedIn(@CookieValue("accessToken") String accessTokenValue) throws Exception{
    }

}
