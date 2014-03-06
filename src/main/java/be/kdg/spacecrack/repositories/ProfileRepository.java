package be.kdg.spacecrack.repositories;

import be.kdg.spacecrack.model.Profile;
import be.kdg.spacecrack.model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
@Component("profileRepository")
public class ProfileRepository implements IProfileRepository {
    @Autowired
    SessionFactory sessionFactory;

    public ProfileRepository() {
    }

    public ProfileRepository(SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;
    }

    public void createProfile(Profile profile) throws Exception {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(profile);
    }

    @Override
    public Profile getContact(User user) {
        Session session = sessionFactory.getCurrentSession();

        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from Profile p where p.profileId = :pId");
        q.setParameter("pId", user.getProfile().getProfileId());
        Profile profile = (Profile) q.uniqueResult();


        return profile;
    }

    @Override
    public void editContact(Profile profile) {
        Session session = sessionFactory.getCurrentSession();

        session.saveOrUpdate(profile);

    }

    @Override
    public Profile getProfileByProfileId(int profileId) {
        Session session = sessionFactory.getCurrentSession();

        Profile profile;

        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from Profile p where p.profileId = :profileId");
        q.setParameter("profileId", profileId);
        profile = (Profile) q.uniqueResult();

        return profile;
    }
}
