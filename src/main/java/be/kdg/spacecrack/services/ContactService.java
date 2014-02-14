package be.kdg.spacecrack.services;

import be.kdg.spacecrack.Exceptions.SpaceCrackAlreadyExistException;
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
@Component
public class ContactService {
    @Autowired
    IContactRepository contactRepository;


    public ContactService() {
        contactRepository = new ContactRepository();
    }

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public void createContact(Contact contact, AccessToken accessToken) throws Exception {
        UserRepository userRepository = new UserRepository();
        User user = userRepository.getUserByAccessToken(accessToken);

        if(user.getContact() == null){
            contact.setUser(user);
            user.setContact(contact);
            userRepository.updateUser(user);
            contactRepository.addContact(contact);
        }else{
            throw new SpaceCrackAlreadyExistException();
        }

    }
}
