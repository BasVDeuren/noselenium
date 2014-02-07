package be.kdg.spacecrack.integrationtests;

import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.UserRepository;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * Created by Ikke on 6-2-14.
 */
public class IntegrationRegisterTests extends BaseFilteredIntegrationTests {

    private User testUser;
    private UserRepository userRepository;

    @Test
    public void testName() throws Exception {
        assertTrue(true);
    }
    /*
    @Before
    public void SetUp(){
        userRepository = new UserRepository();
    }

    @Test
    public void testNewUser() throws Exception {
        String name ="testUsername";
        String pw = "testPassword";
        String pwherhalen = "testPassword";
        String email = "testemail";

        User user = new User(name, pw, pwherhalen, email);
        userRepository.saveUser(user);

        User userDb = userRepository.containsUsername(name);
        assertEquals("User should be created", user.getUserId(), userDb.getUserId());

    }

    @Test(expected = NullPointerException.class)
    public void testNewBadUser() throws Exception {
        String name ="testUsername";
        String pw = "testPassword";
        String pwherhalen = "testBadPasswoord";
        String email = "testemail";

        User user = new User(name, pw, pwherhalen, email);

        userRepository.saveUser(user);

        User userDb = userRepository.containsUsername(name);

        userDb.getToken();
    }

    @Test
    public void test2UsersWithSameUsername() throws Exception {
        String name ="testUsername";
        String pw = "testPassword";
        String pwherhalen = "testPassword";
        String email = "testemail";

        String name2 ="testUsername";
        String pw2 = "testPassword2";
        String pwherhalen2 = "testPassword2";
        String email2 = "testemail2";

        userRepository = new UserRepository();

        User user = new User(name, pw, pwherhalen, email);

        userRepository.saveUser(user);

        user = new User(name2, pw2, pwherhalen2, email2);

        userRepository.saveUser(user);

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        Transaction tx = session.beginTransaction();

        Query q = session.createQuery("from User u where u.username = :username");
        q.setParameter("username", name);
        List users = q.list();
        tx.commit();
        assertEquals("Er zou maar 1 user met een bepaalde username mogen zijn", 1, users.size());
    }

    @After
    public void tearDown(){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();

        Query q = session.createQuery("delete from User");
        q.executeUpdate();

        tx.commit();
    }*/
}
