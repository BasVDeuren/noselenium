package be.kdg.spacecrack.services;

import be.kdg.spacecrack.Exceptions.SpaceCrackAlreadyExistsException;
import be.kdg.spacecrack.Exceptions.SpaceCrackUnauthorizedException;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.Profile;
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
public class ContactService implements IContactService {
    @Autowired
    IContactRepository contactRepository;

    UserRepository userRepository = new UserRepository();;


    public ContactService() {
        contactRepository = new ContactRepository();
    }

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public void createContact(Profile profile, User user) throws Exception {

        if(user.getProfile() == null){
            profile.setUser(user);
            user.setProfile(profile);
            userRepository.updateUser(user);
            contactRepository.addContact(profile);
        }else{
            throw new SpaceCrackAlreadyExistsException();
        }

    }

    @Override
    public void editContact(Profile profile, AccessToken accessToken) throws Exception {
        User user = userRepository.getUserByAccessToken(accessToken);
        if(user.getProfile() == profile){
            contactRepository.editContact(profile);
        }else{
            throw new SpaceCrackUnauthorizedException();
        }
    }
}
