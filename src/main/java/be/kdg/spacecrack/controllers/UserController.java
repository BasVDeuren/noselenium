package be.kdg.spacecrack.controllers;

import be.kdg.spacecrack.Exceptions.SpaceCrackNotAcceptableException;
import be.kdg.spacecrack.Exceptions.SpaceCrackUnauthorizedException;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.modelwrapper.UserWrapper;
import be.kdg.spacecrack.services.IAuthorizationService;
import be.kdg.spacecrack.services.IUserService;
import org.codehaus.jackson.JsonParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
@Component("userController")
@Controller
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IAuthorizationService authorizationService;


    public UserController() {

    }

    public UserController(IUserService userService, IAuthorizationService authorizationService) {
        this.userService = userService;

        this.authorizationService = authorizationService;
    }

    @RequestMapping(value="/user",method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public AccessToken registerUser(@RequestBody UserWrapper userWrapper) throws Exception {
        AccessToken accessToken;
        User userByUsername = userService.getUserByUsername(userWrapper.getUsername());

        if (userByUsername == null) {
            if (userWrapper.getPassword().equals(userWrapper.getPasswordRepeated())) {
                userService.registerUser(userWrapper.getUsername(), userWrapper.getPassword(), userWrapper.getEmail());
                accessToken =  authorizationService.login(userService.getUserByUsername(userWrapper.getUsername()));
            } else {
                throw new SpaceCrackNotAcceptableException("Password and repeat password aren't equal");
            }
        } else {
            throw new SpaceCrackNotAcceptableException("Username already in use!");
        }
        return accessToken;
    }

    @RequestMapping(value = "/auth/user", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public void editUser(@RequestBody UserWrapper userWrapper, @CookieValue("accessToken") String accessTokenValue) throws Exception {

        User user = null;


        AccessToken accessToken = authorizationService.getAccessTokenByValue(accessTokenValue);
        user = userService.getUserByAccessToken(accessToken);


        if (userWrapper.getPassword().equals(userWrapper.getPasswordRepeated())) {
            user.setPassword(userWrapper.getPassword());
            user.setEmail(userWrapper.getEmail());
            user.setUsername(userWrapper.getUsername());
            userService.updateUser(user);
        } else {
            throw new SpaceCrackNotAcceptableException("Passwords should be the same!");
        }

    }

    @RequestMapping(value = "/auth/user",method = RequestMethod.GET)
    @ResponseBody
    public User getUserByToken(@CookieValue("accessToken") String cookieAccessTokenvalue) throws Exception {
        User user = null;

        try {
            user = userService.getUserByAccessToken(authorizationService.getAccessTokenByValue(cookieAccessTokenvalue));
        } catch (JsonParseException ex) {
            throw new SpaceCrackUnauthorizedException();
        }
        if (user == null) {
            throw new SpaceCrackUnauthorizedException();
        }
        return user;
    }
}
