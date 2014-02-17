package be.kdg.spacecrack.services;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.Profile;
import be.kdg.spacecrack.model.User;

public interface IContactService {
    void createContact(Profile profile, User user) throws Exception;

    void editContact(Profile profile, AccessToken accessToken) throws Exception;
}
