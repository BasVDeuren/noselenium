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
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

/**
 * Created by Ikke on 9-2-14.
 */
public class UserControllerTest {

    private UserController userController;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        userController = new UserController(new UserRepository(), new TokenController());

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
        //  userController = new UserController(new UserRepository());
        userController.registerUser(new UserWrapper("username", "password", "password", "email"));

        userController.registerUser(new UserWrapper("username", "password2", "password2", "email2"));


        //assertEquals("Should return first registered user", "password", userController.getUserByUsername("username").getPassword());
    }

    @Test
    public void editUser_ValidFields_UserEdited() throws Exception {
        //   userController = new UserController(new UserRepository());
        userController.registerUser(new UserWrapper("username", "password", "password", "email"));

        userController.editUser( new UserWrapper("username", "newPassword", "newPassword", "newEmail"));
        UserRepository userRepository = new UserRepository();
        assertEquals("password should be changed", "newPassword", userRepository.getUserByUsername("username").getPassword());
        assertEquals("email should be changed", "newEmail", userRepository.getUserByUsername("username").getEmail());
    }

    //Todo: fix
    @Test
    public void EditUser_BadRepeatPassword_SpaceCrackNotAcceptableException() throws Exception {
        expectedEx.expect(SpaceCrackNotAcceptableException.class);
        expectedEx.expectMessage("Passwords should be the same!");
        //  userController = new UserController(new UserRepository());
        userController.registerUser(new UserWrapper("username", "password", "password", "email"));
        userController.editUser(new UserWrapper("username", "newPassword", "newBadRepeatedPassword", "newEmail"));
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
