package be.kdg.spacecrack.repositories;

import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
public interface IUserRepository {

    User getUser(User user);

    void addUser(String username, String password, String email);

    User getUserByUsername(String username) throws Exception;

    void updateUser(User user);

    User getUserByAccessToken(AccessToken accessToken);
}
