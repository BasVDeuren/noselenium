package be.kdg.spacecrack.services;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.Profile;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("userService")
public class UserService implements IUserService {

    @Autowired
    UserRepository userRepository;

    public UserService() {
        userRepository = new UserRepository();
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
    public void registerUser(String username, String password, String email) throws Exception {
        User user = userRepository.addUser(username, password, email);
        Profile profile = new Profile();

        ProfileService profileService = new ProfileService();
        profileService.createProfile(profile, user);
    }

    @Override
    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    @Override
    public List<User> getUsersByString(String username) throws Exception {
        return userRepository.getUsersByString(username);
    }
}
