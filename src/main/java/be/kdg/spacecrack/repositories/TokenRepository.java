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

    @Autowired
    private ITokenStringGenerator generator;

    public TokenRepository() {
        generator = new TokenStringGenerator();
    }

    public TokenRepository(ITokenStringGenerator generator) {

        this.generator = generator;
    }


    @Override
    public AccessToken getAccessTokenByValue(String value) {
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
                throw new RuntimeException(ex);
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
}
