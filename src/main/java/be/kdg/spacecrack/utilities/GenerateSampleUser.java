package be.kdg.spacecrack.utilities;

import be.kdg.spacecrack.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Created by Ikke on 7-2-14.
 */
public class GenerateSampleUser {
        public static void createUser(){
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            Transaction tx = session.beginTransaction();

            //  Game game = new Game();
            User user = new User("test","test");
            session.saveOrUpdate(user);
            tx.commit();
        }
}
