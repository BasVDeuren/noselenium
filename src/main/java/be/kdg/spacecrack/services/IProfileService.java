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

public interface IProfileService {
    void createProfile(Profile profile, User user) throws Exception;

    void editProfile(Profile profile, AccessToken accessToken) throws Exception;
}
