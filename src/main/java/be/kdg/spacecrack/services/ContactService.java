package be.kdg.spacecrack.services;

import be.kdg.spacecrack.Exceptions.SpaceCrackAlreadyExistException;
import be.kdg.spacecrack.Exceptions.SpaceCrackUnauthorizedException;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.Contact;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.ContactRepository;
import be.kdg.spacecrack.repositories.IContactRepository;
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
@Component("contactService")
public class ContactService {
    @Autowired
    IContactRepository contactRepository;

    UserRepository userRepository = new UserRepository();;


    public ContactService() {
        contactRepository = new ContactRepository();
    }

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public void createContact(Contact contact, User user) throws Exception {

        if(user.getContact() == null){
            contact.setUser(user);
            user.setContact(contact);
            userRepository.updateUser(user);
            contactRepository.addContact(contact);
        }else{
            throw new SpaceCrackAlreadyExistException();
        }

    }

    public void editContact(Contact contact, AccessToken accessToken) throws Exception {
        User user = userRepository.getUserByAccessToken(accessToken);
        if(user.getContact() == contact){
            contactRepository.editContact(contact);
        }else{
            throw new SpaceCrackUnauthorizedException();
        }
    }
}
