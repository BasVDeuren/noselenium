package be.kdg.spacecrack.controllers;

import be.kdg.spacecrack.Exceptions.UserNotFoundException;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.utilities.HibernateUtil;
import be.kdg.spacecrack.utilities.ITokenStringGenerator;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Tim on 3/02/14.
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

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        org.hibernate.Transaction tx = session.beginTransaction();

        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from User u where u.name = :name and u.password = :password");
        q.setParameter("name", user.getName());
        q.setParameter("password", user.getPassword());
        User dbUser = (User) q.uniqueResult();


        if (dbUser == null) {
            tx.commit();
            throw new UserNotFoundException();
        }


        AccessToken accessToken = dbUser.getToken();
        if (accessToken == null) {
            String tokenvalue = generator.generateTokenString();
            accessToken = new AccessToken(tokenvalue);
            dbUser.setToken(accessToken);
        }
        //  session.saveOrUpdate(accessToken);
        session.saveOrUpdate(dbUser);
        tx.commit();
        return accessToken;
    }


}
