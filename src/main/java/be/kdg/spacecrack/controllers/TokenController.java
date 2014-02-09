package be.kdg.spacecrack.controllers;

import be.kdg.spacecrack.Exceptions.InvalidTokenHeaderException;
import be.kdg.spacecrack.Exceptions.SpaceCrackUnauthorizedException;
import be.kdg.spacecrack.Exceptions.SpaceCrackUnexpectedException;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.ITokenRepository;
import be.kdg.spacecrack.repositories.IUserRepository;
import be.kdg.spacecrack.repositories.TokenRepository;
import be.kdg.spacecrack.repositories.UserRepository;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
@Controller
@RequestMapping("/api/accesstokens")
public class TokenController {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ITokenRepository tokenRepository;

    static Logger logger = LoggerFactory.getLogger(TokenController.class);

    public TokenController() {
        userRepository = new UserRepository();
        tokenRepository = new TokenRepository();
    }

    public TokenController(IUserRepository userRepository, ITokenRepository tokenRepository) {
        this.userRepository = userRepository;

        this.tokenRepository = tokenRepository;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    AccessToken getToken(@RequestBody User user) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
       try{
        Transaction tx = session.beginTransaction();
        try {


            @SuppressWarnings("JpaQlInspection") Query query = session.createQuery("from User u where u.username = :testusername");
            query.setParameter("testusername", "test");
            if(query.list().size() <1)
            {
                session.saveOrUpdate(new User("test","test"));
            }
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            //e.printStackTrace();
        }
       }finally {
           HibernateUtil.close(session);
       }
        User dbUser;
        try {
            dbUser = userRepository.getUser(user);
        } catch (Exception e) {

            logger.error("Exception while getting user in getToken: ", e);

            throw new SpaceCrackUnexpectedException("Unexpected exception occurred while logging in");

        }

        if (dbUser == null) {
            throw new SpaceCrackUnauthorizedException();
        }

        AccessToken accessToken;
        try {
            accessToken = tokenRepository.getAccessToken(dbUser);
        } catch (Exception e) {
            throw new SpaceCrackUnexpectedException("Unexpected exception occurred while logging in");
        }
        return accessToken;
    }

    @RequestMapping(method = RequestMethod.DELETE, consumes = "application/json")
    public void Logout(@RequestHeader("token") String tokenjson) {
        ObjectMapper objectMapper = new ObjectMapper();

        AccessToken accessToken;
        try {
            accessToken = objectMapper.readValue(tokenjson, AccessToken.class);
        } catch (IOException e) {
            throw new InvalidTokenHeaderException();
        }
        try {
            userRepository.DeleteAccessToken(accessToken);
        } catch (Exception ex) {
            throw new SpaceCrackUnexpectedException("Unexpected exception happened while logging out");
        }


    }


}
