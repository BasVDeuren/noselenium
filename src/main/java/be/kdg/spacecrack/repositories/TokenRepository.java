package be.kdg.spacecrack.repositories;

import be.kdg.spacecrack.Exceptions.SpaceCrackUnexpectedException;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.utilities.HibernateUtil;
import be.kdg.spacecrack.utilities.ITokenStringGenerator;
import be.kdg.spacecrack.utilities.TokenStringGenerator;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
    private ITokenStringGenerator generator;

    public TokenRepository() {
        generator = new TokenStringGenerator();
    }

    public TokenRepository(ITokenStringGenerator generator) {

        this.generator = generator;
    }


    @Override
    public AccessToken getAccessTokenByValue(String value) throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        AccessToken accessToken = null;
        try {
            Transaction tx = session.beginTransaction();
            try {
                @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from AccessToken a where a.value = :value");
                q.setParameter("value", value);
                accessToken = (AccessToken) q.uniqueResult();
                tx.commit();
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.close(session);
        }

        return accessToken;
    }


    @Override
    public void saveAccessToken(User dbUser, AccessToken accessToken) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        try {
            Transaction tx = session.beginTransaction();
            try {


                session.saveOrUpdate(accessToken);

                session.saveOrUpdate(dbUser);
                tx.commit();
            } catch (Exception ex) {
                tx.rollback();
                throw new SpaceCrackUnexpectedException("Unexpected exception in saveAccessToken()");
            }

        } finally {
            HibernateUtil.close(session);
        }
    }

    @Override
    public void deleteAccessToken(AccessToken accessToken) throws Exception {
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

            } catch (RuntimeException ex) {
                logger.error("Unexpected while Deleting Accesstoken database (deleteAccessToken)", ex);
                tx.rollback();
                throw new SpaceCrackUnexpectedException("Unexpected while retrieving user from database");
            }
        } finally {
            HibernateUtil.close(session);
        }
    }
}
