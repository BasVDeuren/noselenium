package be.kdg.spacecrack.repositories;

import be.kdg.spacecrack.model.Contact;
import be.kdg.spacecrack.model.User;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
public interface IContactRepository {
    public void addContact(Contact contact) throws Exception;

    public Contact getContact(User user);
}
