package be.kdg.spacecrack;

import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Ikke on 6-2-14.
 */
public class RegisterTest extends TestWithFilteredMockMVC{

    private User testUser;

    @Test
    public void testNewUser() throws Exception {
        String name ="testUsername";
        String pw = "testPassword";
        String pwherhalen = "testPassword";
        String email = "testemail";
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();

        User user = new User(name, pw, pwherhalen, email);

        session.saveOrUpdate(user);
        tx.commit();

        Session session2 = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx2 = session2.beginTransaction();


        Query q = session2.createQuery("from User u where u.username = :username");
        q.setParameter("username", name);

        User userDb;
        userDb = (User)q.uniqueResult();
        tx2.commit();
        assertEquals("User should be created", user.getUserId(), userDb.getUserId());

    }

    @Test(expected = NullPointerException.class)
    public void testNewBadUser() throws Exception {
        String name ="testUsername";
        String pw = "testPassword";
        String pwherhalen = "testBadPasswoord";
        String email = "testemail";
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();

        User user = new User(name, pw, pwherhalen, email);

        session.saveOrUpdate(user);
        tx.commit();

        Session session2 = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx2 = session2.beginTransaction();

        Query q = session2.createQuery("from User u where u.username = :username");
        q.setParameter("username", name);

        User userDb;
        userDb = (User)q.uniqueResult();
        tx2.commit();

        userDb.getToken();
    }

    /*@Test
    public void test2UsersWithSameUsername() throws Exception {
        String name ="testUsername";
        String pw = "testPassword";
        String pwherhalen = "testBadPasswoord";
        String email = "testemail";
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();

        User user = new User(name, pw, pwherhalen, email);

        session.saveOrUpdate(user);
        tx.commit();

        String name2 ="testUsername";
        String pw2 = "testPassword2";
        String pwherhalen2 = "testBadPasswoord2";
        String email2 = "testemail2";
        Session session2 = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx2 = session.beginTransaction();

        User user2 = new User(name, pw, pwherhalen, email);

        session2.saveOrUpdate(user2);
        tx2.commit();

        Session session3 = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx3 = session3.beginTransaction();

        Query q = session3.createQuery("from User u where u.username = :username");
    }*/

    @After
    public void tearDown(){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();

        Query q = session.createQuery("delete from User");
        q.executeUpdate();

        tx.commit();
    }
}
