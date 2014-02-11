package be.kdg.spacecrack.unittests;

import be.kdg.spacecrack.Exceptions.SpaceCrackNotAcceptableException;
import be.kdg.spacecrack.controllers.ITokenController;
import be.kdg.spacecrack.controllers.TokenController;
import be.kdg.spacecrack.controllers.UserController;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.modelwrapper.UserWrapper;
import be.kdg.spacecrack.repositories.IUserRepository;
import be.kdg.spacecrack.repositories.UserRepository;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.when;

/**
 * Created by Ikke on 9-2-14.
 */
public class UserControllerTest {

    private UserController userController;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    private IUserRepository userRepository;
    private ITokenController tokenController;

    @Before
    public void setUp() throws Exception {
        userRepository = mock(IUserRepository.class);
        tokenController = mock(ITokenController.class);
        userController = new UserController(userRepository, tokenController);

    }

    @Test
    public void RegisterUser_validUser_usercreated() throws Exception {
        IUserRepository userRepository = mock(IUserRepository.class);
        ITokenController tokenController = mock(ITokenController.class);
        AccessToken expected = new AccessToken("test1234");
        stub(tokenController.getToken(any(User.class))).toReturn(expected);
        UserWrapper userWrapper = new UserWrapper("username", "password", "password", "email");

        UserController controllerWithMockedTokenController = new UserController(userRepository, tokenController);
        AccessToken actual = controllerWithMockedTokenController.registerUser(userWrapper);
        assertEquals("Accesstoken should be returned after registrating. ",expected,actual);

    }

    @Test(expected = SpaceCrackNotAcceptableException.class)
    public void RegisterUser_BadRepeatPassword_SpaceCrackNotAcceptableException() throws Exception {
        userController = new UserController(new UserRepository(), new TokenController());
        User user = new User("username", "password", "email");
        userController.registerUser(new UserWrapper("username", "password", "badRepeat", "email"));

    }

    @Test
    public void RegisterUser_ExistingUsername_SpaceCrackNotAcceptableException() throws Exception {
        expectedEx.expect(SpaceCrackNotAcceptableException.class);
        expectedEx.expectMessage("Username already in use!");

        userController.registerUser(new UserWrapper("username", "password", "password", "email"));
        stub(userRepository.getUserByUsername("username")).toReturn(new User());
        userController.registerUser(new UserWrapper("username", "password2", "password2", "email2"));


        //assertEquals("Should return first registered user", "password", userController.getUserByUsername("username").getPassword());
    }

    @Test
    public void editUser_ValidFields_UserEdited() throws Exception {
        //   userController = new UserController(new UserRepository());
        ObjectMapper objectMapper = new ObjectMapper();


        User user = new User("username", "password", "email");
        when(userRepository.getUserByAccessToken(any(AccessToken.class))).thenReturn(user);

        userController.editUser(new UserWrapper("username", "password", "password", "email"), objectMapper.writeValueAsString(new AccessToken("accesstoken1234")));

        Mockito.verify(userRepository, VerificationModeFactory.times(1)).updateUser(user);
    }

    //Todo: fix
    @Test
    public void EditUser_BadRepeatPassword_SpaceCrackNotAcceptableException() throws Exception {
        expectedEx.expect(SpaceCrackNotAcceptableException.class);
        expectedEx.expectMessage("Passwords should be the same!");

        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User("username", "password", "email");
        when(userRepository.getUserByAccessToken(any(AccessToken.class))).thenReturn(user);

        userController.registerUser(new UserWrapper("username", "password", "password", "email"));
        userController.editUser(new UserWrapper("username", "newPassword", "newBadRepeatedPassword", "newEmail"), objectMapper.writeValueAsString(new AccessToken("accesstoken1234")));
    }

    @After
    public void tearDown() throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Transaction tx = session.beginTransaction();
            ;
            try {
                @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("delete from User");
                q.executeUpdate();
                tx.commit();
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.close(session);
        }
    }
}
