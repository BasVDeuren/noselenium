package be.kdg.spacecrack.controllers;

import be.kdg.spacecrack.Exceptions.InvalidTokenHeaderException;
import be.kdg.spacecrack.Exceptions.SpaceCrackUnauthorizedException;
import be.kdg.spacecrack.Exceptions.SpaceCrackUnexpectedException;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.IUserRepository;
import be.kdg.spacecrack.utilities.HibernateUtil;
import be.kdg.spacecrack.utilities.ITokenStringGenerator;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
    private ITokenStringGenerator generator;
    @Autowired
    private IUserRepository userRepository;

    static Logger logger = Logger.getLogger(TokenController.class);

    public TokenController() {
    }

    public TokenController(IUserRepository userRepository, ITokenStringGenerator generator) {
        this.userRepository = userRepository;
        this.generator = generator;
    }


    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    AccessToken getToken(@RequestBody User user) {
        User dbUser = null;
        try {
            dbUser = userRepository.getUser(user);
        } catch (Exception e) {

            logger.error("Exception while getting user in getToken: ", e);

            throw new SpaceCrackUnexpectedException("Unexpected exception occurred while logging in");

        }

        if (dbUser == null) {
            throw new SpaceCrackUnauthorizedException();
        }

        AccessToken accessToken = null;
        try {
            accessToken = getAccessToken(dbUser);
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

    private AccessToken getAccessToken(User dbUser) throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        AccessToken accessToken;
        try {
            Transaction tx = session.beginTransaction();
            try {

                accessToken = dbUser.getToken();
                if (accessToken == null) {
                    String tokenvalue = generator.generateTokenString();
                    accessToken = new AccessToken(tokenvalue);
                    dbUser.setToken(accessToken);
                }
                session.saveOrUpdate(accessToken);
                session.saveOrUpdate(dbUser);
                tx.commit();
            } catch (Exception ex) {
                tx.rollback();
                throw ex;
            }

        } finally {
            HibernateUtil.close(session);
        }
        return accessToken;
    }


}
