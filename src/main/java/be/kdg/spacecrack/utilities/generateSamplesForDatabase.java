package be.kdg.spacecrack.utilities;

import be.kdg.spacecrack.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Created by Ikke on 3-2-14.
 */
public class generateSamplesForDatabase {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();

      //  Game game = new Game();
        User user = new User("test","test");
        session.saveOrUpdate(user);
        tx.commit();
    }
}
