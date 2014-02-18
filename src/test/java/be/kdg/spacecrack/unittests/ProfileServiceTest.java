package be.kdg.spacecrack.unittests;

import be.kdg.spacecrack.Exceptions.SpaceCrackAlreadyExistsException;
import be.kdg.spacecrack.controllers.TokenController;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.Profile;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.ProfileRepository;
import be.kdg.spacecrack.repositories.TokenRepository;
import be.kdg.spacecrack.repositories.UserRepository;
import be.kdg.spacecrack.services.AuthorizationService;
import be.kdg.spacecrack.services.ProfileService;
import be.kdg.spacecrack.utilities.HibernateUtil;
import be.kdg.spacecrack.utilities.TokenStringGenerator;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.verification.VerificationModeFactory;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.mockito.Mockito.*;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
public class ProfileServiceTest {


    private TokenController tokenController;
    UserRepository userRepository;

    @Before
    public void setUp() throws Exception {

        TokenStringGenerator generator = new TokenStringGenerator();
        TokenRepository tokenRepository = new TokenRepository();
        userRepository = mock(UserRepository.class);
        tokenController = new TokenController(new AuthorizationService(tokenRepository, userRepository, generator ));

    }

    @Test
    public void testCreateContact() throws Exception {
        Session session;
        Transaction tx;

        ProfileRepository profileRepository = mock(ProfileRepository.class);
        ProfileService contactService = new ProfileService(profileRepository, userRepository);

        User user = new User("username", "password", "email");
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        tx = session.beginTransaction();
        session.saveOrUpdate(user);
        tx.commit();

        stub(userRepository.getUser(user)).toReturn(user);
        AccessToken accessToken = tokenController.login(user);

        Calendar calendar = new GregorianCalendar(2013,1,5);

        Profile profile = new Profile("firstname","lastname","email", calendar.getTime(),"image");
        contactService.createProfile(profile, user);
        verify(profileRepository, VerificationModeFactory.times(1)).createProfile(profile);
    }

    @Test(expected = SpaceCrackAlreadyExistsException.class)
    public void testCreateExtraContact_notPossible() throws Exception {
        Session session;
        Transaction tx;

        ProfileRepository profileRepository = mock(ProfileRepository.class);
        ProfileService profileService = new ProfileService(profileRepository, userRepository);

        User user = new User("username", "password", "email");
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        tx = session.beginTransaction();
        session.saveOrUpdate(user);
        tx.commit();

        stub(userRepository.getUser(user)).toReturn(user);
        AccessToken accessToken = tokenController.login(user);
        Calendar calendar = new GregorianCalendar(2013,1,5);

        Profile profile = new Profile("firstname","lastname","email", calendar.getTime(),"image");
        Profile profile2 = new Profile("firstname","lastname","email", calendar.getTime(),"image");
        profileService.createProfile(profile, user);
        profileService.createProfile(profile2, user);


    }

    @Test
    public void testEditProfile_validProfile() throws Exception {
        ProfileRepository profileRepository = mock(ProfileRepository.class);
        ProfileService profileService = new ProfileService(profileRepository, userRepository);
        tokenController = mock(TokenController.class);


        User user = new User("username", "password", "email");

        AccessToken token = new AccessToken("accesstoken123");
        stub(tokenController.login(user)).toReturn(token);

        AccessToken accessToken = tokenController.login(user);

        Calendar calendar = new GregorianCalendar(2013,2,12);
        Profile profile = new Profile("firstname","lastname","email", calendar.getTime() ,"image");
        profileService.createProfile(profile, user);
        profile.setFirstname("newFirstname");
        stub(userRepository.getUserByAccessToken(token)).toReturn(user);
        profileService.editProfile(profile, accessToken);

        verify(profileRepository, VerificationModeFactory.times(1)).editContact(profile);
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
