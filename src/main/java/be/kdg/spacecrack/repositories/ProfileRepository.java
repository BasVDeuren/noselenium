package be.kdg.spacecrack.repositories;

import be.kdg.spacecrack.model.Profile;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
@Component("contactRepository")
public class ProfileRepository implements IProfileRepository {
    public void createProfile(Profile profile) throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try{
        Transaction tx = session.beginTransaction();
        try{
        session.saveOrUpdate(profile);
        tx.commit();
        }catch (RuntimeException ex){
            tx.rollback();
            throw ex;
        }
        }finally {
            HibernateUtil.close(session);
        }

    }

    @Override
    public Profile getContact(User user) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from Contact c where c.user = :user");
        q.setParameter("user", user);
        Profile profile = (Profile) q.uniqueResult();
        tx.commit();

        return profile;
    }

    @Override
    public void editContact(Profile profile) {

    }
}
