package be.kdg.spacecrack.repositories;

import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

/**
 * Created by Ikke on 6-2-14.
 */
@Component("userRepository")
public class UserRepository implements IUserRepository {
      Logger logger = Logger.getLogger(UserRepository.class);

    @Override
    public void DeleteAccessToken(AccessToken accessToken) throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        try {
            Transaction tx = session.beginTransaction();
            try {
                @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from AccessToken a where a.accessTokenId = :id");
                q.setParameter("id", accessToken.getAccessTokenId());
                AccessToken dbAccessToken = (AccessToken) q.uniqueResult();
                dbAccessToken.getUser().setToken(null);
                session.delete(dbAccessToken);
                tx.commit();

            } catch (Exception ex) {
                logger.error("Unexpected while retrieving user from database (getUser())", ex);
                tx.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.close(session);
        }
    }

    @Override
    public User getUser(User user) throws Exception {
        User dbUser;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Transaction tx = session.beginTransaction();
            try {
                @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from User u where u.username = :username and u.password = :password");
                q.setParameter("username", user.getUsername());
                q.setParameter("password", user.getPassword());
                dbUser = (User) q.uniqueResult();

                tx.commit();
            } catch (Exception ex) {
                logger.error("Unexpected while retrieving user from database (getUser())", ex);
                tx.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.close(session);
        }
        return dbUser;
    }
}
