package be.kdg.spacecrack.unittests;

import be.kdg.spacecrack.model.Contact;
import be.kdg.spacecrack.repositories.ContactRepository;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Arno on 13/02/14.
 */
public class ContactRepositoryTest {
    @Test
    public void testAddContact() throws Exception {
        Contact contact = new Contact("email", "image", "firstname", "lastname", "birthdate");
        ContactRepository contactRepository = new ContactRepository();
        contactRepository.addContact(contact);
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();

        @SuppressWarnings("JpaQlInspection")Query q = session.createQuery("FROM Contact c WHERE c = :contact");
        q.setParameter("contact", contact);

        Contact actual = (Contact) q.uniqueResult();
        tx.commit();
        assertEquals("Should be in the db",contact.getContactId(), actual.getContactId());
    }
}
