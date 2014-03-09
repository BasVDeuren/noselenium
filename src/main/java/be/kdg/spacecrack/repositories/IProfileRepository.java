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
public interface IProfileRepository {
    public void createProfile(Profile profile);

    public Profile getContact(User user);

    void editContact(Profile profile);

    Profile getProfileByProfileId(int profileId);
}
