package be.kdg.spacecrack;

import be.kdg.spacecrack.controllers.GameController;
import be.kdg.spacecrack.model.Game;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Created by Ikke on 3-2-14.
 */
public class StartGameTest {


    private final GameController gameController = new GameController();
    private Game testgame;

    @Before
    public void setUp() throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        testgame = new Game();
        session.saveOrUpdate(testgame);
        tx.commit();


    }

    @Test
    public void testNewGame() throws Exception {
        int gameId = gameController.createNewGame();
        int gameId2 = gameController.createNewGame();

        assertNotSame("Make sure that a new Id is given.", gameId, gameId2);
    }

    @Test
    public void testGetGame() throws Exception {
        int gameId = gameController.createNewGame();
        Game resultGame = gameController.getGame(gameId);

        assertEquals("ResultGame should be the same as expectedGame", gameId, resultGame.getGameId());
    }

    @Test
    public void test2ExistingGames() throws Exception {
        int gameId = gameController.createNewGame();
        gameController.createNewGame();

        assertEquals(gameId ,gameController.getGame(gameId).getGameId());
    }

    @After
    public void tearDown() throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();

        session.delete(testgame);
        tx.commit();

    }
}
