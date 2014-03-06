package be.kdg.spacecrack.unittests;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Game;
import be.kdg.spacecrack.model.Player;
import be.kdg.spacecrack.model.Profile;
import be.kdg.spacecrack.repositories.GameRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;


public class GameRepositoryTest extends BaseUnitTest {
    @Autowired
    SessionFactory sessionFactory;

    @Test
    @Transactional
    public void GetAllGamesByProfile() throws Exception {

        Profile profile1;
        Profile profile2;
        Game expected;
        Session session = sessionFactory.getCurrentSession();


        profile1 = new Profile();
        profile2 = new Profile();
        session.saveOrUpdate(profile1);
        session.saveOrUpdate(profile2);
        Player player1 = new Player(profile1);
        Player player2 = new Player(profile2);
        expected = new Game();
        player1.setGame(expected);
        player2.setGame(expected);
        session.saveOrUpdate(player1);
        session.saveOrUpdate(player2);


        expected.getPlayers().add(player1);
        expected.getPlayers().add(player2);
        session.saveOrUpdate(expected);
        //

        GameRepository gameRepository = new GameRepository(sessionFactory);


        List<Game> games = gameRepository.getGamesByProfile(profile1);

        Game actualGame = games.get(0);
        assertEquals(expected.getGameId(), actualGame.getGameId());
    }

    @Transactional
    @Test
    public void getGameByGameId() throws Exception {
        Profile profile1;
        Profile profile2;
        Game expected;
        Session session = sessionFactory.getCurrentSession();


        profile1 = new Profile();
        profile2 = new Profile();
        session.saveOrUpdate(profile1);
        session.saveOrUpdate(profile2);
        Player player1 = new Player(profile1);
        Player player2 = new Player(profile2);
        expected = new Game();
        player1.setGame(expected);
        player2.setGame(expected);
        session.saveOrUpdate(player1);
        session.saveOrUpdate(player2);


        expected.getPlayers().add(player1);
        expected.getPlayers().add(player2);
        session.saveOrUpdate(expected);


        GameRepository gameRepository = new GameRepository(sessionFactory);

        int expectedId = gameRepository.createGame(expected);

        Game actual = gameRepository.getGameByGameId(expectedId);

        assertEquals("GameId from repository should be the same as actual gameId", expectedId, actual.getGameId());
    }
}
