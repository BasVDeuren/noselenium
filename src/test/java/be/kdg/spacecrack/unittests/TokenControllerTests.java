package be.kdg.spacecrack.unittests;

import be.kdg.spacecrack.Exceptions.SpaceCrackUnauthorizedException;
import be.kdg.spacecrack.Exceptions.SpaceCrackUnexpectedException;
import be.kdg.spacecrack.controllers.TokenController;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.IUserRepository;
import be.kdg.spacecrack.repositories.TokenRepository;
import be.kdg.spacecrack.repositories.UserRepository;
import be.kdg.spacecrack.utilities.HibernateUtil;
import be.kdg.spacecrack.utilities.ITokenStringGenerator;
import be.kdg.spacecrack.utilities.TokenStringGenerator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.Assert.assertEquals;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
public class TokenControllerTests{


    private TokenController tokenController;
    private TokenStringGenerator fixedSeedGenerator;
    private User testUser;

    @Before
    public void setUp() throws Exception {
        fixedSeedGenerator = new TokenStringGenerator(1234);
        tokenController = new TokenController(new UserRepository(), new TokenRepository(fixedSeedGenerator));

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        testUser = new User("testUsername", "testPassword");
        session.saveOrUpdate(testUser);
        tx.commit();
    }

    @Test(expected = SpaceCrackUnauthorizedException.class)
    public void testRequestAccessToken_InvalidUser_UserNotFoundException() {

        String name = "badUser";
        String pw = "badPw";
        User user = new User(name, pw);
        tokenController.login(user);

    }

    @Test
    public void testRequestAccessToken_ValidUser_Ok()
    {
        ITokenStringGenerator mockTokenGenerator = Mockito.mock(ITokenStringGenerator.class);
        TokenController tokenControllerWithMockedGenerator = new TokenController(new UserRepository(),new TokenRepository(mockTokenGenerator));
        String name ="testUsername";
        String pw = "testPassword";
        User user = new User(name, pw);
        String expectedTokenValue = "testtokenvalue1234";
        Mockito.stub(mockTokenGenerator.generateTokenString()).toReturn(expectedTokenValue);
        AccessToken token = tokenControllerWithMockedGenerator.login(user);


        assertEquals("Token value should be testtokenvalue1234", expectedTokenValue, token.getValue() );
    }

    @Test(expected = SpaceCrackUnexpectedException.class)
    public void testgetUser() throws Exception {
        IUserRepository repository = Mockito.mock(IUserRepository.class);
        User user = new User("testUsername", "testPassword");
        Mockito.stub(repository.getUser(user)).toThrow(new Exception());
        TokenController tokenController1 = new TokenController(repository, new TokenRepository(fixedSeedGenerator));
        tokenController1.login(user);

    }


    @After
    public void tearDown() throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        session.delete(testUser);
        tx.commit();
    }
}
