package be.kdg.spacecrack.unittests;

import be.kdg.spacecrack.controllers.UserController;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Ikke on 9-2-14.
 */
public class UserControllerTest {

    private UserController userController;

    /*@Test
    public void testRegisterUser() throws Exception {
        IUserRepository userRepository = Mockito.mock(IUserRepository.class);
        userController = new UserController(userRepository);
        User user = new User("username", "password", "email");
        userController.registerUser("username", "password", "passwordRepeated", "email");
        assertEquals("User has to be created",  user.getUsername(),  userController.getUser(user.getUsername()).getUsername());
    }*/

    @Test
    public void testName() throws Exception {
        assertTrue(true);
    }
}
