package be.kdg.spacecrack.unittests;

import be.kdg.spacecrack.model.Profile;
import be.kdg.spacecrack.repositories.ProfileRepository;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

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
public class ProfileRepositoryTest {
    @Test
    public void testAddContact() throws Exception {
        Calendar calendar = new GregorianCalendar(2014,2,12);
        Profile profile = new Profile("firstname","lastname","email", calendar.getTime(),"image");
        ProfileRepository contactRepository = new ProfileRepository();
        contactRepository.createProfile(profile);
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Profile actual;
        try {
            Transaction tx = session.beginTransaction();

            try {
                @SuppressWarnings("JpaQlInspection")Query q = session.createQuery("FROM Profile p WHERE p = :profile");
                q.setParameter("profile", profile);

                actual = (Profile) q.uniqueResult();
                tx.commit();
            } catch (RuntimeException e) {
                tx.rollback();
                throw e;
            }
        } finally {
            HibernateUtil.close(session);
        }
        assertEquals("Should be in the db", profile.getProfileId(), actual.getProfileId());
    }
}
