package be.kdg.spacecrack.services;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;

public interface IAuthorizationService {
    public AccessToken getAccessTokenByValue(String accessTokenValue);

    void createTestUser();

    AccessToken login(User user);

    void logout(String accessTokenValue);

    User getUserByAccessTokenValue(String accessTokenValue);
}
