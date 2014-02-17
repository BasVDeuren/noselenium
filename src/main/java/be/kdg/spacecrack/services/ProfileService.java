package be.kdg.spacecrack.services;

import be.kdg.spacecrack.Exceptions.SpaceCrackAlreadyExistsException;
import be.kdg.spacecrack.Exceptions.SpaceCrackUnauthorizedException;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.Profile;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.IProfileRepository;
import be.kdg.spacecrack.repositories.IUserRepository;
import be.kdg.spacecrack.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
@Component("contactService")
public class ProfileService implements IProfileService {
    @Autowired
    IProfileRepository contactRepository;

    @Autowired
    IUserRepository userRepository;


    public ProfileService() {
        contactRepository = new ProfileRepository();
    }

    public ProfileService(ProfileRepository contactRepository, IUserRepository userRepository) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createProfile(Profile profile, User user) throws Exception {

        if(user.getProfile() == null){
            profile.setUser(user);
            user.setProfile(profile);
            contactRepository.createProfile(profile);
            userRepository.updateUser(user);

        }else{
            throw new SpaceCrackAlreadyExistsException();
        }

    }

    @Override
    public void editProfile(Profile profile, AccessToken accessToken) throws Exception {
        User user = userRepository.getUserByAccessToken(accessToken);
        if(user.getProfile() == profile){
            contactRepository.editContact(profile);
        }else{
            throw new SpaceCrackUnauthorizedException();
        }
    }


}
