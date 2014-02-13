package be.kdg.spacecrack.repositories;

import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import org.springframework.stereotype.Component;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
@Component("userRepository")
public interface IUserRepository {
    void DeleteAccessToken(AccessToken accessToken) throws Exception;

    User getUser(User user) throws Exception;

    void addUser(String username, String password, String email);

    User getUserByUsername(String username) throws Exception;

    void updateUser(User user);

    User getUserByAccessToken(AccessToken accessToken) throws Exception;
}
