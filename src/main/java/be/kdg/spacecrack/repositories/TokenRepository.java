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

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
@Component("tokenRepository")
public class TokenRepository implements ITokenRepository {
    Logger logger = LoggerFactory.getLogger(TokenRepository.class);
    @Autowired
    SessionFactory sessionFactory;

    public TokenRepository() {
    }

    public TokenRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public AccessToken getAccessTokenByValue(String value) throws Exception {
        Session session = sessionFactory.getCurrentSession();
        AccessToken accessToken = null;

        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from AccessToken a where a.value = :value");
        q.setParameter("value", value);
        accessToken = (AccessToken) q.uniqueResult();
        return accessToken;
    }


    @Override
    public void saveAccessToken(User dbUser, AccessToken accessToken) {
        Session session = sessionFactory.getCurrentSession();


        session.saveOrUpdate(accessToken);

        session.saveOrUpdate(dbUser);


    }

    @Override
    public void deleteAccessToken(AccessToken accessToken) throws Exception {
        Session session = sessionFactory.getCurrentSession();


        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from AccessToken a where a.accessTokenId = :id and a.value = :value");
        q.setParameter("id", accessToken.getAccessTokenId());
        q.setParameter("value", accessToken.getValue());
        AccessToken dbAccessToken = (AccessToken) q.uniqueResult();
        if (dbAccessToken != null) {
            dbAccessToken.getUser().setToken(null);
            session.delete(dbAccessToken);
        }


    }


}
