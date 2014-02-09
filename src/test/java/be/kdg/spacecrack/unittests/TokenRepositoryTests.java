package be.kdg.spacecrack.unittests;

import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.ITokenRepository;
import be.kdg.spacecrack.repositories.TokenRepository;
import be.kdg.spacecrack.utilities.HibernateUtil;
import be.kdg.spacecrack.utilities.ITokenStringGenerator;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.verification.VerificationModeFactory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

/**
 * Created by Ikke on 7-2-14.
 */
public class TokenRepositoryTests {


    private User testUser;



    @Before
    public void setUp() throws Exception {


        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        testUser = new User("testUsername", "testPassword");
        session.saveOrUpdate(testUser);
        tx.commit();
    }

    @Test
    public void testGetAccessToken() throws Exception {
        ITokenStringGenerator mockedGenerator = mock(ITokenStringGenerator.class);
        String testToken123 = "testToken123";
        stub( mockedGenerator.generateTokenString()).toReturn(testToken123);
        ITokenRepository tokenRepository = new TokenRepository(mockedGenerator);
        AccessToken accessToken = tokenRepository.getAccessToken(testUser);

        assertEquals("Tokens should be equal",testToken123, accessToken.getValue());

    }

    @Test
    public void testGetAccessTokenTwice() throws Exception {
        ITokenStringGenerator mockedGenerator = mock(ITokenStringGenerator.class);
        String testToken123 = "testToken123";
        stub( mockedGenerator.generateTokenString()).toReturn(testToken123);

        ITokenRepository tokenRepository = new TokenRepository(mockedGenerator);
        tokenRepository.getAccessToken(testUser);
       //AccessToken accessToken = tokenRepository.getAccessToken(testUser);

        tokenRepository.getAccessToken(testUser);
        verify(mockedGenerator, VerificationModeFactory.times(1)).generateTokenString();


    }

    @After
    public void tearDown() throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("delete from User");
        q.executeUpdate();
        tx.commit();
    }
}
