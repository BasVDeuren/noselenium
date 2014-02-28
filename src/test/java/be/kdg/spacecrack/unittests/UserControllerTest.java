package be.kdg.spacecrack.unittests;

import be.kdg.spacecrack.Exceptions.SpaceCrackNotAcceptableException;
import be.kdg.spacecrack.Exceptions.SpaceCrackUnauthorizedException;
import be.kdg.spacecrack.controllers.UserController;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.IUserRepository;
import be.kdg.spacecrack.services.IAuthorizationService;
import be.kdg.spacecrack.services.IUserService;
import be.kdg.spacecrack.services.UserService;
import be.kdg.spacecrack.utilities.HibernateUtil;
import be.kdg.spacecrack.viewmodels.UserWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

//import org.codehaus.jackson.map.ObjectMapper;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
public class UserControllerTest {

    private UserController userController;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    private IUserRepository userRepository;

    private IAuthorizationService tokenService;
    private IUserService userService;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        userRepository = mock(IUserRepository.class);

        tokenService = mock(IAuthorizationService.class);

        userService = mock(UserService.class);
        userController = new UserController(userService, tokenService);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void RegisterUser_validUser_usercreated() throws Exception {
        AccessToken expected = new AccessToken("test1234");
        stub(tokenService.login(any(User.class))).toReturn(expected);
        UserWrapper userWrapper = new UserWrapper("username", "password", "password", "email");

        AccessToken actual = userController.registerUser(userWrapper);
        assertEquals("Accesstoken should be returned after registering. ", expected, actual);

    }

    @Test(expected = SpaceCrackNotAcceptableException.class)
    public void RegisterUser_BadRepeatPassword_SpaceCrackNotAcceptableException() throws Exception {
        User user = new User("username", "password", "email");
        userController.registerUser(new UserWrapper("username", "password", "badRepeat", "email"));
    }

    @Test
    public void RegisterUser_ExistingUsername_SpaceCrackNotAcceptableException() throws Exception {
        expectedEx.expect(SpaceCrackNotAcceptableException.class);
        expectedEx.expectMessage("Username already in use!");

        userController.registerUser(new UserWrapper("username", "password", "password", "email"));
        stub(userService.getUserByUsername("username")).toReturn(new User());
        userController.registerUser(new UserWrapper("username", "password2", "password2", "email2"));


        //assertEquals("Should return first registered user", "password", userController.getUserByUsername("username").getPassword());
    }

    @Test
    public void editUser_ValidFields_UserEdited() throws Exception {
        //   userController = new UserController(new UserRepository());
        ObjectMapper objectMapper = new ObjectMapper();


        User user = new User("username", "password", "email");
        when(userService.getUserByAccessToken(any(AccessToken.class))).thenReturn(user);

        userController.editUser(new UserWrapper("username", "password", "password", "email"), objectMapper.writeValueAsString(new AccessToken("accesstoken1234")));

        Mockito.verify(userService, VerificationModeFactory.times(1)).updateUser(user);
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

    @Test
    public void testGetUser_validToken_User() throws Exception {
        User user = new User("username", "password", "email");
        AccessToken accessToken = new AccessToken("accesstoken123");
        user.setToken(accessToken);

        stub(userService.getUserByAccessToken(accessToken)).toReturn(user);
        stub(tokenService.getAccessTokenByValue(accessToken.getValue())).toReturn(accessToken);

        //User actual = userController.getUserByToken(accessToken);
        User actual = userController.getUserByToken(accessToken.getValue());
        //User actual = userController.getUserByToken();

        User expected = user;

        assertEquals("User from usercontroller should be the same as from db", expected, actual);
    }

    @Test
    public void testGetUser_NotInDbToken_SpaceCrackNotAcceptableException() throws Exception {
        expectedEx.expect(SpaceCrackUnauthorizedException.class);

        AccessToken invalidAccessToken = new AccessToken("TokenNotInDb");

        when(userRepository.getUserByAccessToken(any(AccessToken.class))).thenReturn(null);

        //User actual = userController.getUserByToken(invalidAccessToken);
        userController.getUserByToken(invalidAccessToken.getValue());
        //userController.getUserByToken();
    }

    /*@Test
    public void testGetUser_TokenInDbButNotFromLoggedOnUser_SpaceCrackNotAcceptableException() throws Exception {
        expectedEx.expect(SpaceCrackNotAcceptableException.class);
        expectedEx.expectMessage("This request is not acceptable!");

        User notUserThatIsRequesting = new User("badUsername", "badPassword", "badEmail");
        AccessToken invalidAccessToken = new AccessToken("TokenNotWithUser");
        notUserThatIsRequesting.setToken(invalidAccessToken);

        User user = new User("username", "password", "email");
        AccessToken accessToken = new AccessToken("validAccessToken");
        user.setToken(accessToken);

        when(userRepository.getUserByAccessToken(any(AccessToken.class))).thenReturn(notUserThatIsRequesting);

        userController.getUserByToken(invalidAccessToken);
    }*/

    @Test
    public void testGetUsers_validUserName_User() throws Exception {
        User user = new User("Jacky", "password", "email");
        List<User> foundUsers = new ArrayList<User>();
        AccessToken accessToken = new AccessToken("accesstoken123");
        user.setToken(accessToken);
        foundUsers.add(user);
        stub(userService.getUsersByString("Jac")).toReturn(foundUsers);
        //stub(tokenService.getAccessTokenByValue(accessToken.getValue())).toReturn(accessToken);

        //User actual = userController.getUserByToken(accessToken);
        List<User> actualUsers = new ArrayList<User>();
        actualUsers = userController.getUsersByString("Jac");
        //User actual = userController.getUserByToken();

        assertEquals("Users from usercontroller should be the same as from db", foundUsers.get(0), actualUsers.get(0));
    }

    @Test
    public void testGetUsers_validEmail_User() throws Exception {
        User user = new User("Tommy", "password", "tommy@gmail.com");
        List<User> foundUsers = new ArrayList<User>();
        AccessToken accessToken = new AccessToken("accesstoken321");
        user.setToken(accessToken);
        foundUsers.add(user);
        stub(userService.getUsersByEmail("tom")).toReturn(foundUsers);
        List<User> actualUsers = new ArrayList<User>();
        actualUsers = userController.getUsersByEmail("tom");

        assertEquals("Users from usercontroller should be the same as from db", foundUsers.get(0), actualUsers.get(0));
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
