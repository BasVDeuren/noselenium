package be.kdg.spacecrack.repositories;

import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Created by Ikke on 6-2-14.
 */
public class UserRepository {
    Session session;
    Transaction tx;
    Query q;

    public void saveUser(User user){
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        tx = session.beginTransaction();
        session.saveOrUpdate(user);
        tx.commit();
    }

    public User containsUsername(String username){
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        tx = session.beginTransaction();
        q = session.createQuery("from User u where u.username = :username");
        q.setParameter("username", username);

        User user = (User)q.uniqueResult();

        tx.commit();

        return user;
    }
}
