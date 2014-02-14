package be.kdg.spacecrack.unittests;

import be.kdg.spacecrack.Exceptions.SpaceCrackAlreadyExistException;
import be.kdg.spacecrack.controllers.TokenController;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.Contact;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.ContactRepository;
import be.kdg.spacecrack.services.ContactService;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.mockito.Mockito.mock;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
public class ContactServiceTest {

    @Test
    public void testCreateContact() throws Exception {
        Session session;
        Transaction tx;

        ContactRepository contactRepository = mock(ContactRepository.class);
        ContactService contactService = new ContactService(contactRepository);

        User user = new User("username", "password", "email");
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        tx = session.beginTransaction();
        session.saveOrUpdate(user);
        tx.commit();

        TokenController tokenController = new TokenController();
        AccessToken accessToken = tokenController.login(user);

        Calendar calendar = new GregorianCalendar(2013,1,5);

        Contact contact = new Contact("firstname","lastname","email", calendar.getTime(),"image");
        contactService.createContact(contact, user);
        Mockito.verify(contactRepository, VerificationModeFactory.times(1)).addContact(contact);
    }

    @Test(expected = SpaceCrackAlreadyExistException.class)
    public void testCreateExtraContact_notPossible() throws Exception {
        Session session;
        Transaction tx;

        ContactRepository contactRepository = mock(ContactRepository.class);
        ContactService contactService = new ContactService(contactRepository);

        User user = new User("username", "password", "email");
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        tx = session.beginTransaction();
        session.saveOrUpdate(user);
        tx.commit();

        TokenController tokenController = new TokenController();
        AccessToken accessToken = tokenController.login(user);
        Calendar calendar = new GregorianCalendar(2013,1,5);

        Contact contact = new Contact("firstname","lastname","email", calendar.getTime(),"image");
        Contact contact2 = new Contact("firstname","lastname","email", calendar.getTime(),"image");
        contactService.createContact(contact, user);
        contactService.createContact(contact2, user);


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
