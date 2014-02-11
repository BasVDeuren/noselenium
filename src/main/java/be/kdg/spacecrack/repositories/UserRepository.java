package be.kdg.spacecrack.repositories;

import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by Ikke on 6-2-14.
 */
@Component("userRepository")
public class UserRepository implements IUserRepository {
    Logger logger = LoggerFactory.getLogger(UserRepository.class);

    @Override
    public void DeleteAccessToken(AccessToken accessToken) throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        try {
            Transaction tx = session.beginTransaction();
            try {
                @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from AccessToken a where a.accessTokenId = :id and a.value = :value");
                q.setParameter("id", accessToken.getAccessTokenId());
                q.setParameter("value", accessToken.getValue());
                AccessToken dbAccessToken = (AccessToken) q.uniqueResult();
                if (dbAccessToken != null) {
                    dbAccessToken.getUser().setToken(null);
                    session.delete(dbAccessToken);
                }

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

    @Override
    public void addUser(String username, String password, String email) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        User user = new User(username, password, email);
        session.saveOrUpdate(user);
        tx.commit();
    }

    @Override
    public User getUserByUsername(String username) throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        User user;
        try {
            Transaction tx = session.beginTransaction();
            try {
                @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from User u where u.username = :username");
                q.setParameter("username", username);
                user = (User) q.uniqueResult();
                tx.commit();
            } catch (Exception ex) {
                logger.error("Unexpected while retrieving user from database (getUser())", ex);
                tx.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.close(session);
        }
        return user;
    }

    @Override
    public void updateUser(User user) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(user);
        tx.commit();
    }

    @Override
    public User getUserByAccessToken(AccessToken accessToken) throws Exception {
        User user;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Transaction tx = session.beginTransaction();
            try{
            @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from User u where u.token = :token");
            q.setParameter("token", accessToken);
                user = (User) q.uniqueResult();
            tx.commit();
            }catch(Exception ex){
                logger.error("Unexpected while retrieving user from database (getUser())", ex);
                tx.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.close(session);
        }
        return user;
    }
}
