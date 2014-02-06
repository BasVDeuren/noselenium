package be.kdg.spacecrack;

import be.kdg.spacecrack.Exceptions.SpaceCrackUnauthorizedException;
import be.kdg.spacecrack.controllers.TokenController;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.UserRepository;
import be.kdg.spacecrack.utilities.TokenStringGenerator;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Ikke on 6-2-14.
 */
public class TokenControllerTests {


    private TokenController tokenController;

    @Before
    public void setUp() throws Exception {
        tokenController = new TokenController(new UserRepository(), new TokenStringGenerator(1234));
    }

    @Test(expected = SpaceCrackUnauthorizedException.class)
    public void testRequestAccessToken_InvalidUser_UserNotFoundException() {

        String name = "badUser";
        String pw = "badPw";
        User user = new User(name, pw);
        tokenController.getToken(user);

    }
}
