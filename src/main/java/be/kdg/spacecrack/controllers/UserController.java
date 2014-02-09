package be.kdg.spacecrack.controllers;

import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.modelwrapper.UserWrapper;
import be.kdg.spacecrack.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Ikke on 9-2-14.
 */
@Controller
@RequestMapping("/api/register")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    public UserController() {

    }

    public UserController(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public AccessToken registerUser(@RequestBody UserWrapper userWrapper) throws Exception {
        AccessToken accessToken = null;
        if (getUserByUsername(userWrapper.getUsername()) == null) {
            if (userWrapper.getPassword().equals(userWrapper.getPasswordRepeated())) {
                userRepository.registerUser(userWrapper.getUsername(), userWrapper.getPassword(), userWrapper.getEmail());
                accessToken = new TokenController().getToken(userRepository.getUserByUsername(userWrapper.getUsername()));
            }
        }
        return accessToken;
    }

    public User getUserByUsername(String username) throws Exception {
        return userRepository.getUserByUsername(username);
    }
}
