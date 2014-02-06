package be.kdg.spacecrack.controllers;

import be.kdg.spacecrack.Exceptions.InvalidTokenHeaderException;
import be.kdg.spacecrack.Exceptions.SpaceCrackUnauthorizedException;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.utilities.HibernateUtil;
import be.kdg.spacecrack.utilities.ITokenStringGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Query;
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
@RequestMapping("/accesstokens")
public class TokenController {
    @Autowired
    private ITokenStringGenerator generator;

    public TokenController() {
    }

    public TokenController(ITokenStringGenerator generator) {
        this.generator = generator;
    }


    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    AccessToken getToken(@RequestBody User user) {

        User dbUser = getUser(user);

        if (dbUser == null) {
            throw new SpaceCrackUnauthorizedException();
        }

        return getAccessToken(dbUser);
    }

    @RequestMapping(method = RequestMethod.DELETE, consumes = "application/json")
    public void Logout(@RequestHeader("token") String tokenjson)  {
        ObjectMapper objectMapper = new ObjectMapper();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            AccessToken accessToken = objectMapper.readValue(tokenjson, AccessToken.class);


            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from AccessToken a where a.accessTokenId = :id");
            q.setParameter("id", accessToken.getAccessTokenId());
            AccessToken dbAccessToken  = (AccessToken) q.uniqueResult();
            dbAccessToken.getUser().setToken(null);
            session.delete(dbAccessToken);
            tx.commit();
        } catch (IOException e) {
            throw new InvalidTokenHeaderException();
        }


    }
    private AccessToken getAccessToken(User dbUser) {
        Session session2 = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx2 = session2.beginTransaction();


        AccessToken accessToken = dbUser.getToken();
        if (accessToken == null) {
            String tokenvalue = generator.generateTokenString();
            accessToken = new AccessToken(tokenvalue);
            dbUser.setToken(accessToken);
        }
        session2.saveOrUpdate(accessToken);
        session2.saveOrUpdate(dbUser);
        tx2.commit();
        return accessToken;
    }

    private User getUser(User user) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();

        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from User u where u.username = :username and u.password = :password");
        q.setParameter("username", user.getUsername());
        q.setParameter("password", user.getPassword());
        User dbUser = (User) q.uniqueResult();
        tx.commit();
        return dbUser;
    }


}
