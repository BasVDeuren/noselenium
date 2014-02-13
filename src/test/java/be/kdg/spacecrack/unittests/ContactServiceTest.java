package be.kdg.spacecrack.unittests;

import be.kdg.spacecrack.Exceptions.SpaceCrackAlreadyExistException;
import be.kdg.spacecrack.controllers.ContactService;
import be.kdg.spacecrack.controllers.TokenController;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.Contact;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.ContactRepository;
import be.kdg.spacecrack.repositories.UserRepository;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;


/**
 * Created by Arno on 13/02/14.
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

        Contact contact = new Contact("email", "image", "firstname", "lastname", "birthdate");
        contactService.createContact(contact, accessToken);
        Mockito.verify(contactRepository, VerificationModeFactory.times(1)).addContact(contact);
    }
//
//    @Test(expected = SpaceCrackAlreadyExistException.class)
//    public void testCreateExtraContact_notPossible() throws Exception {
//        Session session;
//        Transaction tx;
//
//        ContactRepository contactRepository = mock(ContactRepository.class);
//        ContactService contactService = new ContactService(contactRepository);
//
//        User user = new User("username", "password", "email");
//        session = HibernateUtil.getSessionFactory().getCurrentSession();
//        tx = session.beginTransaction();
//        session.saveOrUpdate(user);
//        tx.commit();
//
//        TokenController tokenController = new TokenController();
//        AccessToken accessToken = tokenController.login(user);
//
//        Contact contact = new Contact("email", "image", "firstname", "lastname", "birthdate");
//        Contact contact2 = new Contact("email", "image", "firstname", "lastname", "birthdate");
//        contactService.createContact(contact, accessToken);
//        contactService.createContact(contact2, accessToken);
//
//
//    }
}
