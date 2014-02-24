package be.kdg.spacecrack.controllers;

import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.services.IAuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;


/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
@Component("tokenController")
@RequestMapping("/accesstokens")
public class TokenController{


    @Autowired
    private IAuthorizationService tokenService;


    static Logger logger = LoggerFactory.getLogger(TokenController.class);

    public TokenController() {

    }

    public TokenController(IAuthorizationService tokenService) {
        this.tokenService = tokenService;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    AccessToken login(@RequestBody User user) {
        tokenService.createTestUsers();
        AccessToken accessToken = tokenService.login(user);

        return accessToken;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public void Logout(@CookieValue("accessToken") String accessTokenValue) throws Exception {
        String substring = accessTokenValue.substring(1, accessTokenValue.length() - 1);
        tokenService.logout(substring);
    }


}
