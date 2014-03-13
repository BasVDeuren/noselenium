package be.kdg.spacecrack.unittests;

import be.kdg.spacecrack.model.Profile;
import be.kdg.spacecrack.repositories.ProfileRepository;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
public class ProfileRepositoryTest extends BaseUnitTest {
    @Test @Transactional
    public void testAddContact() throws Exception {
        Calendar calendar = new GregorianCalendar(2014,2,12);
        Profile profile = new Profile("firstname","lastname", calendar.getTime(),"image");
        ProfileRepository contactRepository = new ProfileRepository(sessionFactory);
        contactRepository.createProfile(profile);
        Session session = sessionFactory.getCurrentSession();
        Profile actual;

        @SuppressWarnings("JpaQlInspection")Query q = session.createQuery("FROM Profile p WHERE p = :profile");
        q.setParameter("profile", profile);
        actual = (Profile) q.uniqueResult();

        assertEquals("Should be in the db", profile.getProfileId(), actual.getProfileId());
    }
}
