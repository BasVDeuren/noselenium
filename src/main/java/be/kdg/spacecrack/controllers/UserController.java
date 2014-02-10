package be.kdg.spacecrack.controllers;

import be.kdg.spacecrack.Exceptions.SpaceCrackNotAcceptableException;
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
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private ITokenController tokenController;


    public UserController() {

    }

    public UserController(IUserRepository userRepository, ITokenController tokenController) {
        this.userRepository = userRepository;
        this.tokenController = tokenController;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public AccessToken registerUser(@RequestBody UserWrapper userWrapper) throws Exception {
        AccessToken accessToken = null;
        User userByUsername = userRepository.getUserByUsername(userWrapper.getUsername());
        if (userByUsername == null) {
            if (userWrapper.getPassword().equals(userWrapper.getPasswordRepeated())) {
                userRepository.addUser(userWrapper.getUsername(), userWrapper.getPassword(), userWrapper.getEmail());
                accessToken = tokenController.getToken(userRepository.getUserByUsername(userWrapper.getUsername()));
            }else{
                throw new SpaceCrackNotAcceptableException("Password and repeat password aren't equal")    ;
            }
        }else{
            throw new SpaceCrackNotAcceptableException("Username already in use!")    ;
        }
        return accessToken;
    }

    public void editUser( UserWrapper userWrapper) throws Exception {
        User user = userRepository.getUserByUsername(userWrapper.getUsername());
        if (userWrapper.getPassword().equals(userWrapper.getPasswordRepeated())) {
            user.setPassword(userWrapper.getPassword());
            user.setEmail(userWrapper.getEmail());
            userRepository.editUser(user);
        }else{
            throw new SpaceCrackNotAcceptableException("Passwords should be the same!");
        }
    }
}
