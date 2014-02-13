package be.kdg.spacecrack.repositories;

import be.kdg.spacecrack.model.Contact;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

/**
 * Created by Arno on 13/02/14.
 */
@Component("contactRepository")
public class ContactRepository implements IContactRepository{
    public void addContact(Contact contact) throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try{
        Transaction tx = session.beginTransaction();
        try{
        session.saveOrUpdate(contact);
        tx.commit();
        }catch (Exception ex){
            tx.rollback();
            throw ex;
        }
        }finally {
            HibernateUtil.close(session);
        }

    }

    @Override
    public Contact getContact(User user) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();

        Contact contact = new Contact();



        return contact;
    }
}
