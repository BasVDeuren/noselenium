package be.kdg.spacecrack.repositories;

import be.kdg.spacecrack.model.Contact;
import be.kdg.spacecrack.model.User;

/**
 * Created by Arno on 13/02/14.
 */
public interface IContactRepository {
    public void addContact(Contact contact) throws Exception;

    public Contact getContact(User user);
}
