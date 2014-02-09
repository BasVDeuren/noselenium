package be.kdg.spacecrack.unittests;

import be.kdg.spacecrack.controllers.UserController;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.modelwrapper.UserWrapper;
import be.kdg.spacecrack.repositories.UserRepository;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Ikke on 9-2-14.
 */
public class UserControllerTest {

    private UserController userController;

    @Test
    public void testRegisterUser() throws Exception {
        userController = new UserController(new UserRepository());
        User user = new User("username", "password", "email");
        userController.registerUser(new UserWrapper("username", "password", "password", "email"));
        assertEquals("User has to be created",  user.getUsername(),  userController.getUserByUsername(user.getUsername()).getUsername());
    }

    @Test(expected = NullPointerException.class)
    public void testRegisterBadRepeatPassword() throws Exception {
        userController = new UserController(new UserRepository());
        User user = new User("username", "password", "email");
        userController.registerUser(new UserWrapper("username", "password", "badRepeat", "email"));
        userController.getUserByUsername(user.getUsername()).getUsername();
    }

    @Test
    public void testCreateUserWithExistingUsername() throws Exception {
        userController = new UserController(new UserRepository());
        userController.registerUser(new UserWrapper("username", "password", "password", "email"));
        userController.registerUser(new UserWrapper("username", "password2", "password2", "email2"));

        assertEquals("Should return first registered user", "password", userController.getUserByUsername("username").getPassword());
    }

    @Test
    public void testEditUser() throws Exception {
        userController = new UserController(new UserRepository());
        userController.registerUser(new UserWrapper("username", "password", "password", "email"));

        userController.editUser("username", new UserWrapper("username", "newPassword", "newPassword", "newEmail"));
        UserRepository userRepository = new UserRepository();
        assertEquals("password should be changed", "newPassword", userRepository.getUserByUsername("username").getPassword());
        assertEquals("email should be changed", "newEmail",userRepository.getUserByUsername("username").getEmail());
    }

    @Test
    public void testEditUserBadPassword() throws Exception {
        userController = new UserController(new UserRepository());
        userController.registerUser(new UserWrapper("username", "password", "password", "email"));

        userController.editUser("username", new UserWrapper("username", "newPassword", "newBadRepeatedPassword", "newEmail"));
        UserRepository userRepository = new UserRepository();
        assertEquals("password shouldn't be changed", "password", userRepository.getUserByUsername("username").getPassword());
        assertEquals("email shouldn't be changed", "email",userRepository.getUserByUsername("username").getEmail());
    }

    @After
    public void tearDown() throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try{
        Transaction tx = session.beginTransaction();;
        try{
        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("delete from User");
        q.executeUpdate();
        tx.commit();
        }catch (Exception ex){
            tx.rollback();
            throw ex;
        }
        }finally {
            HibernateUtil.close(session);
        }
    }
}
