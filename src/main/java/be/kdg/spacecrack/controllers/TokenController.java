package be.kdg.spacecrack.controllers;

import be.kdg.spacecrack.Exceptions.InvalidTokenHeaderException;
import be.kdg.spacecrack.Exceptions.SpaceCrackUnauthorizedException;
import be.kdg.spacecrack.Exceptions.SpaceCrackUnexpectedException;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.ITokenRepository;
import be.kdg.spacecrack.repositories.IUserRepository;
import be.kdg.spacecrack.utilities.GenerateSamplesForDatabase;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
@Controller
@RequestMapping("/api/accesstokens")
public class TokenController {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ITokenRepository tokenRepository;

    static Logger logger = LoggerFactory.getLogger(TokenController.class);

    public TokenController() {
    }

    public TokenController(IUserRepository userRepository, ITokenRepository tokenRepository) {
        this.userRepository = userRepository;

        this.tokenRepository = tokenRepository;
    }


    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    AccessToken getToken(@RequestBody User user) {
        try {
            User testUserForDeployment = userRepository.getUser(new User("test", "test"));
            if(testUserForDeployment == null){
                GenerateSamplesForDatabase.createUser();
            }
        } catch (Exception e) {
            GenerateSamplesForDatabase.createUser();
            //e.printStackTrace();
        }
        User dbUser = null;
        try {
            dbUser = userRepository.getUser(user);
        } catch (Exception e) {

            logger.error("Exception while getting user in getToken: ", e);

            throw new SpaceCrackUnexpectedException("Unexpected exception occurred while logging in");

        }

        if (dbUser == null) {
            throw new SpaceCrackUnauthorizedException();
        }

        AccessToken accessToken = null;
        try {
            accessToken = tokenRepository.getAccessToken(dbUser);
        } catch (Exception e) {
            throw new SpaceCrackUnexpectedException("Unexpected exception occurred while logging in");
        }
        return accessToken;
    }

    @RequestMapping(method = RequestMethod.DELETE, consumes = "application/json")
    public void Logout(@RequestHeader("token") String tokenjson) {
        ObjectMapper objectMapper = new ObjectMapper();

        AccessToken accessToken;
        try {
            accessToken = objectMapper.readValue(tokenjson, AccessToken.class);
        } catch (IOException e) {
            throw new InvalidTokenHeaderException();
        }
        try {
            userRepository.DeleteAccessToken(accessToken);
        } catch (Exception ex) {
            throw new SpaceCrackUnexpectedException("Unexpected exception happened while logging out");
        }


    }


}
