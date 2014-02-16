package be.kdg.spacecrack.services;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public void registerUser(String username, String password, String email) {
        userRepository.addUser(username, password, email);
    }

    @Override
    public void updateUser(User user) {
        userRepository.updateUser(user);
    }
}
