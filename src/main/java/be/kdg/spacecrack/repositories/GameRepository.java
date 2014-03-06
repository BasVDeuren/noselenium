package be.kdg.spacecrack.repositories;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Game;
import be.kdg.spacecrack.model.Player;
import be.kdg.spacecrack.model.Profile;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("gameRepository")
public class GameRepository implements IGameRepository {


    public GameRepository() {
    }


    @Autowired
    SessionFactory sessionFactory;


    public GameRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public int createGame(Game game) {
        Session session = sessionFactory.getCurrentSession();

        session.saveOrUpdate(game);


        return game.getGameId();
    }

    @Override
    public int updateGame(Game game) {
        return createGame(game); // Because saveorupdate
    }

    @Override
    public List<Game> getGamesByProfile(Profile profile) {
        Session session = sessionFactory.getCurrentSession();
        List<Game> games;

        @SuppressWarnings("JpaQlInspection") Query query = session.createQuery("FROM Game game WHERE game.gameId in (SELECT player.game.gameId FROM Player player where player.profile = :profile)");
        query.setParameter("profile", profile);

        games = (List<Game>) query.list();

        return games;
    }

    @Override
    public Game getGameByGameId(int gameId) {
        Session session = sessionFactory.getCurrentSession();
        Game game;

        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from Game g where g.gameId = :gameId");
        q.setParameter("gameId", gameId);
        game = (Game) q.uniqueResult();

        return game;
    }

    @Override
    public Game getGameByPlayer(Player player) {
        Session session = sessionFactory.getCurrentSession();
        Game game;

        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from Game g where :player in (from Player p where p.game = g)");
        q.setParameter("player", player);
        game = (Game) q.uniqueResult();

        return game;
    }


}
