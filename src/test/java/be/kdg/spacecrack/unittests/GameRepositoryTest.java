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
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class GameRepositoryTest {
    @Test
    public void GetAllGamesByProfile() throws Exception {

        Profile profile;
        Game expected;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        try {
            Transaction tx = session.beginTransaction();
            try {
                profile = new Profile();
                session.saveOrUpdate(profile);
                Player player1 = new Player(profile);
                session.saveOrUpdate(player1);
                expected = new Game();
                session.saveOrUpdate(expected);
                expected.setPlayer1(player1);
                session.saveOrUpdate(expected);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw new RuntimeException(e);
            }
        } finally {
            HibernateUtil.close(session);
        }
        GameRepository gameRepository = new GameRepository();

        int expectedId = gameRepository.createGame(expected);

        List<Game> games = gameRepository.getGamesByProfile(profile);

        Game actualGame = games.get(0);
        assertEquals(expectedId, actualGame.getGameId());
    }
}
