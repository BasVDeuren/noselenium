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
public interface ITokenRepository {

    AccessToken getAccessTokenByValue(String value) throws Exception;

    void saveAccessToken(User dbUser, AccessToken accessToken);

    void deleteAccessToken(AccessToken accessToken) throws Exception;
}
