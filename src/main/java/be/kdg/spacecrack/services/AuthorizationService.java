package be.kdg.spacecrack.services;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.Exceptions.SpaceCrackUnauthorizedException;
import be.kdg.spacecrack.Exceptions.SpaceCrackUnexpectedException;
import be.kdg.spacecrack.controllers.TokenController;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.Profile;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.ITokenRepository;
import be.kdg.spacecrack.repositories.IUserRepository;
import be.kdg.spacecrack.utilities.ITokenStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("authorizationService")
@Transactional
public class AuthorizationService implements IAuthorizationService {

    @Autowired
    ITokenRepository tokenRepository;
    @Autowired
    IUserRepository userRepository;



    @Autowired
    private ITokenStringGenerator tokenStringGenerator;

    static Logger logger = LoggerFactory.getLogger(TokenController.class);

    public AuthorizationService() {
    }

    public AuthorizationService(ITokenRepository tokenRepository, IUserRepository userRepository, ITokenStringGenerator tokenStringGenerator) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.tokenStringGenerator = tokenStringGenerator;
    }

    @Override
    public AccessToken getAccessTokenByValue(String accessTokenValue) {
        try {
            return tokenRepository.getAccessTokenByValue(accessTokenValue);
        } catch (Exception e) {
            throw new SpaceCrackUnauthorizedException("Unauthorized Request");
        }
    }


    @Override
    public void createTestUsers() {

        createTestUser("test@gmail.com", "test", "test");
        createTestUser("opponentje@gmail.com", "OpponentTest", "test");
    }

    private void createTestUser(String testemail, String testUsername, String testPassword) {
        List<User> list = userRepository.findUsersByEmailPart(testemail);
        if (list.size() < 1) {



            Profile profile = new Profile();

            User user =  new User(testUsername, testPassword, testemail);
            user.setProfile(profile);
            profile.setUser(user);
            userRepository.createUser(user);
        }


    }

    @Override
    public AccessToken login(User user) {
        User dbUser;

        dbUser = userRepository.getUser(user);


        if (dbUser == null) {
            throw new SpaceCrackUnauthorizedException();
        }
        AccessToken accessToken = dbUser.getToken();

        if (accessToken == null) {
            String tokenvalue = tokenStringGenerator.generateTokenString();
            accessToken = new AccessToken(tokenvalue);
            accessToken.setUser(dbUser);
            dbUser.setToken(accessToken);

            tokenRepository.saveAccessToken(dbUser, accessToken);
        }
        return accessToken;
    }


    @Override
    public void logout(String accessTokenValue) {
        AccessToken accessToken = null;
        try {
            accessToken = tokenRepository.getAccessTokenByValue(accessTokenValue);
        } catch (Exception e) {

        }

        try {
            tokenRepository.deleteAccessToken(accessToken);
        } catch (Exception ex) {
            throw new SpaceCrackUnexpectedException("Unexpected exception happened while logging out");
        }
    }

    @Override
    public User getUserByAccessTokenValue(String accessTokenValue) {
        return userRepository.getUserByAccessToken(getAccessTokenByValue(accessTokenValue));
    }
}
