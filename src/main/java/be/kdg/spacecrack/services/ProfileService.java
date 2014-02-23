package be.kdg.spacecrack.services;

import be.kdg.spacecrack.Exceptions.SpaceCrackAlreadyExistsException;
import be.kdg.spacecrack.model.Profile;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.IProfileRepository;
import be.kdg.spacecrack.repositories.IUserRepository;
import be.kdg.spacecrack.repositories.ProfileRepository;
import be.kdg.spacecrack.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
@Component("profileService")
public class ProfileService implements IProfileService {
    @Autowired
    IProfileRepository profileRepository;

    @Autowired
    IUserRepository userRepository;


    public ProfileService() {
        this.profileRepository = new ProfileRepository();
        this.userRepository = new UserRepository();
    }

    public ProfileService(ProfileRepository profileRepository, IUserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createProfile(Profile profile, User user) throws Exception {

        if(user.getProfile() == null){
            profile.setUser(user);
            user.setProfile(profile);
            profileRepository.createProfile(profile);
            userRepository.updateUser(user);

        }else{
            throw new SpaceCrackAlreadyExistsException();
        }

    }

    @Override
    public void editProfile(Profile profile) throws Exception {
        profileRepository.editContact(profile);
    }

    @Override
    public Profile getProfileByUser(User user) {
        return profileRepository.getContact(user);
    }

    @Override
    public Profile getProfileByProfileId(int profileId) {
        return profileRepository.getProfileByProfileId(profileId);
    }
}
