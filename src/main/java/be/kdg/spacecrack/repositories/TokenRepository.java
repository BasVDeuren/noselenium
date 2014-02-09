package be.kdg.spacecrack.repositories;

import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.utilities.HibernateUtil;
import be.kdg.spacecrack.utilities.ITokenStringGenerator;
import be.kdg.spacecrack.utilities.TokenStringGenerator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Ikke on 7-2-14.
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
    public AccessToken getAccessToken(User dbUser) throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        AccessToken accessToken;
        try {
            Transaction tx = session.beginTransaction();
            try {

                accessToken = dbUser.getToken();
                if (accessToken == null) {
                    String tokenvalue = generator.generateTokenString();
                    accessToken = new AccessToken(tokenvalue);
                    dbUser.setToken(accessToken);
                }
                session.saveOrUpdate(accessToken);
                session.saveOrUpdate(dbUser);
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
}
