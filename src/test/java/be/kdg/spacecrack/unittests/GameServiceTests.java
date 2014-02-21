package be.kdg.spacecrack.unittests;

import be.kdg.spacecrack.Exceptions.SpaceCrackNotAcceptableException;
import be.kdg.spacecrack.model.*;
import be.kdg.spacecrack.repositories.*;
import be.kdg.spacecrack.services.GameService;
import be.kdg.spacecrack.services.MapService;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
public class GameServiceTests {

    private GameService gameService;
    private User user;

    @Before
    public void setUp() throws Exception {

        gameService = new GameService(new MapService(),new PlanetRepository(), new ColonyRepository(), new ShipRepository(), new PlayerRepository(), new GameRepository());
        user = new User();
    }

    @Test
    public void createGame_SinglePlayer_GameWithColonyCreated() throws Exception {
        Profile profile =new Profile();
        Game game = gameService.createGame(profile);
        Player player1 = game.getPlayer1();
        List<Player> players = profile.getPlayers();
        assertTrue(players.contains(player1));
        assertTrue(player1.getColonies().size() == 1);
        assertTrue(player1.getColonies().get(0).getPlanet().getName().equals("a"));
        assertTrue(player1.getShips().size()==1);
        assertTrue(player1.getShips().get(0).getPlanet().getName().equals("a"));
       // assertTrue(player1.getColonies().get(0).getPl)

    }

    private Game creategame()
    {
        Profile profile =new Profile();
        user.setProfile(profile);
        Game game = gameService.createGame(profile);
        return game;
    }
    @Test
    public void moveShip_validPlanet_shipmoved() throws Exception {
        Game game = creategame();

        Ship ship = game.getPlayer1().getShips().get(0);

        gameService.moveShip(ship, "b");
        Planet shipLocation = gameService.getShipLocationByShipId(ship.getShipId());

        assertEquals("b", shipLocation.getName());

    }

    @Test(expected = SpaceCrackNotAcceptableException.class)
    public void moveShip_invalidPlanet_shipNotMoved() throws Exception {
        Game game = creategame();

        Ship ship = game.getPlayer1().getShips().get(0);
        gameService.moveShip(ship, "f");
        Planet shipLocation = gameService.getShipLocationByShipId(ship.getShipId());

        assertEquals("a", shipLocation.getName());

    }

    @Test
    public void MoveShipAndCreateColony_validPlanetNoColonyOnPlanet_ColonyPlaced() throws Exception {
        Game game = creategame();

        Ship ship = game.getPlayer1().getShips().get(0);

        gameService.moveShip(ship, "b");

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        Query q = session.createQuery("from Ship s where s.shipId = :shipId");
        q.setParameter("shipId", ship.getShipId());
        Ship shipDb = (Ship) q.uniqueResult();
        tx.commit();

        List<Colony> colonies = shipDb.getPlayer().getColonies();
        assertEquals("The player should have 2 colonies", 2, colonies.size());
        assertEquals("The second colony the player should have is on planet b.", "b", colonies.get(1).getPlanetName());
    }

    @Test(expected = SpaceCrackNotAcceptableException.class, timeout = 20000)
    public void MoveShipWithNoCommandPoints_SpaceCrackNotAcceptableException() throws Exception {
        Game game = creategame();

        Ship ship = game.getPlayer1().getShips().get(0);

        while(true){
            gameService.moveShip(ship, "b");
            gameService.moveShip(ship, "c");
        }
    }

    @Test
    public void GetAllGamesFromPlayer() throws Exception {
        Game game = creategame();

        IGameRepository gameRepository = mock(IGameRepository.class);
        ArrayList<Game> expected = new ArrayList<Game>();
        expected.add(new Game());
        expected.add(new Game());
        stub(gameRepository.getGamesByProfile(user.getProfile())).toReturn(expected);
        GameService gameService1 = new GameService(new MapService(), new PlanetRepository(), new ColonyRepository(), new ShipRepository(), new PlayerRepository(), gameRepository);

        List<Game> actual = gameService1.getGames(user);

        verify(gameRepository, VerificationModeFactory.times(1)).getGamesByProfile(user.getProfile());
        assertEquals(expected, actual);
    }

    @Test
    public void getGameFromPlayer() throws Exception {
        Game expected = creategame();

        IGameRepository gameRepository = mock(IGameRepository.class);
        GameService gameService1 = new GameService(new MapService(), new PlanetRepository(), new ColonyRepository(), new ShipRepository(), new PlayerRepository(), gameRepository);
        stub(gameRepository.getGameByGameId(expected.getGameId())).toReturn(expected);

        Game actual = gameService1.getGameByGameId(expected.getGameId());

        verify(gameRepository, VerificationModeFactory.times(1)).getGameByGameId(expected.getGameId());
        assertEquals("Actual gameId should be the same as the expected gameId", expected.getGameId(), actual.getGameId());
    }
}
