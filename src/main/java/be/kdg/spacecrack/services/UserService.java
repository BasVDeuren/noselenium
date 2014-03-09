package be.kdg.spacecrack.services;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.Exceptions.SpaceCrackAlreadyExistsException;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.Profile;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.IProfileRepository;
import be.kdg.spacecrack.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Component("userService")
@Transactional
public class UserService implements IUserService {

    @Autowired
    IUserRepository userRepository;

    @Autowired
    IProfileRepository profileRepository;

    public UserService() {

    }


    public UserService(IUserRepository userRepository, IProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }


    @Override
    public User getUserByAccessToken(AccessToken accessToken) throws Exception {
        return userRepository.getUserByAccessToken(accessToken);
    }

    @Override
    public User getUserByUsername(String username) throws Exception {
        return userRepository.getUserByUsername(username);
    }

    @Override
    public void registerUser(String username, String password, String email)  {
        User userByUsername = userRepository.getUserByUsername(username);
        if(userByUsername != null)
        {
            throw new SpaceCrackAlreadyExistsException();
        }

        User userByEmail = userRepository.getUserByEmail(email);
        if(userByEmail != null)
        {
            throw new SpaceCrackAlreadyExistsException();
        }
        User user = userRepository.addUser(username, password, email);
        Profile profile = new Profile();


        if(user.getProfile() == null){
            profile.setUser(user);
            user.setProfile(profile);
            profileRepository.createProfile(profile);
            userRepository.updateUser(user);

        }else{
            //throw new SpaceCrackAlreadyExistsException();
        }

    }

    @Override
    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    @Override
    public List<User> getUsersByString(String username) throws Exception {
        return userRepository.findUsersByUsernamePart(username);
    }

    @Override
    public List<User> getUsersByEmail(String email) throws Exception {
        return userRepository.findUsersByEmailPart(email);
    }

    @Override
    public User getRandomUser(int userId) throws Exception {
        User foundUser;
        List<User> users = userRepository.getLoggedInUsers();
        do{
            Random random = new Random();
            foundUser = users.get(random.nextInt(users.size()));
        }while(foundUser.getUserId() == userId);
        return foundUser;
    }

}
