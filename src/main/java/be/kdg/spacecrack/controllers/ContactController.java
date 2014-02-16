package be.kdg.spacecrack.controllers;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Contact;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.jsonviewmodels.ContactWrapper;
import be.kdg.spacecrack.repositories.TokenRepository;
import be.kdg.spacecrack.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/auth/contact")
public class ContactController {
    @Autowired
    ContactService contactService;
    @Autowired
    IUserService userService;
    @Autowired
    IAuthorizationService tokenService;

    public ContactController() {

    }

    public ContactController(ContactService contactService, UserService userService, TokenRepository tokenRepository, IAuthorizationService tokenService){
        this.contactService = contactService;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public void createContact(@RequestBody ContactWrapper contactWrapper, @CookieValue("accessToken") String accessTokenValue) throws Exception {


        User user = tokenService.getUserByAccessToken(accessTokenValue, this);

        Date date = new SimpleDateFormat("dd-mm-yyyy").parse(contactWrapper.getDayOfBirth());
        Contact contact = new Contact(contactWrapper.getFirstname(), contactWrapper.getLastname(), contactWrapper.getEmail(), date, contactWrapper.getImage());

        contactService.createContact(contact, user);
    }

}
