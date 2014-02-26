package be.kdg.spacecrack.repositories;

import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;

import java.util.List;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
public interface IUserRepository {

    User getUser(User user);

    User addUser(String username, String password, String email);

    User getUserByUsername(String username) throws Exception;

    void updateUser(User user);

    User getUserByAccessToken(AccessToken accessToken);
    List<User> getUsersByString(String username) throws Exception;
}
