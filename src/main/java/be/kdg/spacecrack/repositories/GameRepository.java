package be.kdg.spacecrack.repositories;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Game;
import be.kdg.spacecrack.model.Profile;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller("gameRepository")
public class GameRepository implements IGameRepository {

    @Override
    public int createGame(Game game) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Transaction tx = session.beginTransaction();
            try {
                session.saveOrUpdate(game);
                tx.commit();
            } catch (RuntimeException e) {
                tx.rollback();
                throw e;
            }
        } finally {
            HibernateUtil.close(session);
        }
        return game.getGameId();
    }

    @Override
    public List<Game> getGamesByProfile(Profile profile) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        List<Game> games;
        try {
            Transaction tx = session.beginTransaction();
            try {
                games = getGamesByProfile(profile, session);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw new RuntimeException(e);
            }
        } finally {
           HibernateUtil.close(session);
        }
        return games;
    }

    private List<Game> getGamesByProfile(Profile profile, Session session) {
        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("select g from Game g where g.player1.profile = :profile");
        q.setParameter("profile", profile);
        return (List<Game>)q.list();
    }


}
