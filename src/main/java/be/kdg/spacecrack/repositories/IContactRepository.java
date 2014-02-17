package be.kdg.spacecrack.repositories;

import be.kdg.spacecrack.model.Profile;
import be.kdg.spacecrack.model.User;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
public interface IContactRepository {
    public void addContact(Profile profile) throws Exception;

    public Profile getContact(User user);

    void editContact(Profile profile);
}
