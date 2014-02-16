package be.kdg.spacecrack.services;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.Contact;
import be.kdg.spacecrack.model.User;

public interface IContactService {
    void createContact(Contact contact, User user) throws Exception;

    void editContact(Contact contact, AccessToken accessToken) throws Exception;
}
