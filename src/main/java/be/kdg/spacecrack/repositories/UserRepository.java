package be.kdg.spacecrack.repositories;

import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    @Autowired
    SessionFactory sessionFactory;

    public UserRepository() {
    }

    public UserRepository(SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;
    }

    @Override
    public User getUser(User user) {
        User dbUser;
        Session session = sessionFactory.getCurrentSession();
        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from User u where u.email = :email and u.password = :password");
        q.setParameter("email", user.getEmail());
        q.setParameter("password", user.getPassword());
        dbUser = (User) q.uniqueResult();

        return dbUser;
    }

    @Override
    public User addUser(String username, String password, String email) {
        Session session = sessionFactory.getCurrentSession();

        User user = new User(username, password, email);
        session.saveOrUpdate(user);

        return user;
    }

    @Override
    public User getUserByUsername(String username){
        Session session = sessionFactory.getCurrentSession();
        User user;

        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from User u where u.username = :username");
        q.setParameter("username", username);
        user = (User) q.uniqueResult();


        return user;
    }


    @Override
    public List<User> findUsersByUsernamePart(String username) throws Exception {
        Session session = sessionFactory.getCurrentSession();
        List<User> foundUsers;

        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from User u where u.username LIKE :username");
        q.setParameter("username", "%" + username + "%");
        foundUsers = q.list();


        return foundUsers;
    }

    @Override
    public List<User> findUsersByEmailPart(String emailPart){
        Session session = sessionFactory.getCurrentSession();
        List<User> foundUsers;

        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from User u where u.email LIKE :email");
        q.setParameter("email", "%" + emailPart + "%");
        foundUsers = q.list();

        return foundUsers;
    }

    @Override
    public void updateUser(User user) {
        Session session = sessionFactory.getCurrentSession();

        session.saveOrUpdate(user);

    }

    @Override
    public void createUser(User user) {
        Session session = sessionFactory.getCurrentSession();

        session.saveOrUpdate(user);

    }
    @Override
    public User getUserByAccessToken(AccessToken accessToken) {
        User user;
        Session session = sessionFactory.getCurrentSession();

        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from User u where u.token = :token");
        q.setParameter("token", accessToken);
        user = (User) q.uniqueResult();

        return user;
    }

    @Override
    public List<User> getLoggedInUsers() throws Exception {
        Session session = sessionFactory.getCurrentSession();
        List<User> foundUsers;

        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from User u WHERE u.token IS NOT NULL ");
        foundUsers = q.list();

        return foundUsers;
    }

    @Override
    public User getUserByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        List<User> foundUsers;

        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from User u WHERE u.email = :email ");
        q.setParameter("email", email);
        User user = (User) q.uniqueResult();

        return user;
    }


}
