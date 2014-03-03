package be.kdg.spacecrack.repositories;

import be.kdg.spacecrack.Exceptions.SpaceCrackUnexpectedException;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
@Component("userRepository")
public class UserRepository implements IUserRepository {
    Logger logger = LoggerFactory.getLogger(UserRepository.class);

    @Override
    public User getUser(User user) {
        User dbUser;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Transaction tx = session.beginTransaction();
            try {
                @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from User u where u.email = :email and u.password = :password");
                q.setParameter("email", user.getEmail());
                q.setParameter("password", user.getPassword());
                dbUser = (User) q.uniqueResult();

                tx.commit();
            } catch (RuntimeException ex) {
                logger.error("Unexpected while retrieving user from database (getUser())", ex);
                tx.rollback();
                throw new SpaceCrackUnexpectedException("Unexpected while retrieving user from database");
            }
        } finally {
            HibernateUtil.close(session);
        }
        return dbUser;
    }

    @Override
    public User addUser(String username, String password, String email) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        User user = new User(username, password, email);
        session.saveOrUpdate(user);
        tx.commit();
        return user;
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
    public List<User> getUsersByString(String username) throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        List<User> foundUsers;
        try {
            Transaction tx = session.beginTransaction();
            try {
                @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from User u where u.username LIKE :username");
                q.setParameter("username", "%" + username + "%");
                foundUsers = q.list();
                tx.commit();
            } catch (Exception ex) {
                logger.error("Unexpected while retrieving user from database (getUsers())", ex);
                tx.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.close(session);
        }
        return foundUsers;
    }

    @Override
    public List<User> getUsersByEmail(String email) throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        List<User> foundUsers;
        try {
            Transaction tx = session.beginTransaction();
            try {
                @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from User u where u.email LIKE :email");
                q.setParameter("email", "%" + email + "%");
                foundUsers = q.list();
                tx.commit();
            } catch (Exception ex) {
                logger.error("Unexpected while retrieving user from database (getUsersByEmail())", ex);
                tx.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.close(session);
        }
        return foundUsers;
    }

    @Override
    public void updateUser(User user) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Transaction tx = session.beginTransaction();
            try {
                session.saveOrUpdate(user);
                tx.commit();
            } catch (RuntimeException ex) {
                tx.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.close(session);
        }
    }

    @Override
    public User getUserByAccessToken(AccessToken accessToken) {
        User user;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Transaction tx = session.beginTransaction();
            try {
                @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from User u where u.token = :token");
                q.setParameter("token", accessToken);
                user = (User) q.uniqueResult();
                tx.commit();
            } catch (Exception ex) {
                logger.error("Unexpected error while retrieving user from database (getUser())", ex);
                tx.rollback();
                throw new SpaceCrackUnexpectedException("Unexpected error");
            }
        } finally {
            HibernateUtil.close(session);
        }
        return user;
    }

    @Override
    public List<User> getUsers() throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        List<User> foundUsers;
        try {
            Transaction tx = session.beginTransaction();
            try {
                @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from User u WHERE u.token IS NOT NULL ");
                foundUsers = q.list();
                tx.commit();
            } catch (Exception ex) {
                logger.error("Unexpected while retrieving user from database (getUsers())", ex);
                tx.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.close(session);
        }
        return foundUsers;
    }



}
