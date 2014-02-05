package be.kdg.spacecrack.controllers;

import be.kdg.spacecrack.model.Game;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
public class GameController {
    List<Game> games;

    public GameController() {
        this.games = new ArrayList<Game>();
    }

    public int createNewGame() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        System.out.println("transaction started");
        Game game;
        game = new Game();
        session.saveOrUpdate(game);
        tx.commit();
        System.out.println("transaction committed");
        return game.getGameId();
    }

    public Game getGame(int gameId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        System.out.println("transaction started");
        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("FROM Game g where g.gameId = :gameId");
        q.setParameter("gameId", gameId);
        Game game = (Game)q.uniqueResult();
        tx.commit();
        System.out.println("transaction committed");
        return game;
    }
}
