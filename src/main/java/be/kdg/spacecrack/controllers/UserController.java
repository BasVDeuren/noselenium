package be.kdg.spacecrack.controllers;

import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Created by Ikke on 9-2-14.
 */
@Controller
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    public UserController(){

    }

    public UserController(IUserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void registerUser(String username, String password, String passwordRepeated, String email) {
        userRepository.registerUser(username, password, email);
    }

    public User getUser(String username) throws Exception {
        return userRepository.getUserByUsername(username);
    }
}
