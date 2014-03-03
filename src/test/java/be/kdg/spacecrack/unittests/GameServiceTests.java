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
import org.mockito.internal.verification.VerificationModeFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

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
    private IPlayerRepository playerRepository;
    private Profile opponentProfile;

    @Before
    public void setUp() throws Exception {
        playerRepository = new PlayerRepository();
        gameService = new GameService(new MapService(),new PlanetRepository(), new ColonyRepository(), new ShipRepository(), playerRepository, new GameRepository());
        user = new User();
        Profile profile =new Profile();
        opponentProfile = new Profile();
        User opponentUser = new User();
        opponentUser.setProfile(opponentProfile);
        user.setProfile(profile);
    }

    private Game creategame()
    {
        int gameId = gameService.createGame(user.getProfile(), "SpaceCrackName", opponentProfile);
        Game game = gameService.getGameByGameId(gameId);
        return game;
    }
    @Test
    public void moveShip_validPlanet_shipmoved() throws Exception {
        Game game = creategame();

        Ship ship = game.getPlayers().get(0).getShips().get(0);

        gameService.moveShip(ship.getShipId(), "b");
        Planet shipLocation = gameService.getShipLocationByShipId(ship.getShipId());

        assertEquals("b", shipLocation.getName());

    }

    @Test(expected = SpaceCrackNotAcceptableException.class)
    public void moveShip_invalidPlanet_shipNotMoved() throws Exception {
        Game game = creategame();

        Ship ship = game.getPlayers().get(0).getShips().get(0);
        gameService.moveShip(ship.getShipId(), "f");
        Planet shipLocation = gameService.getShipLocationByShipId(ship.getShipId());

        assertEquals("a", shipLocation.getName());

    }

    @Test
    public void MoveShipAndCreateColony_validPlanetNoColonyOnPlanet_ColonyPlaced() throws Exception {
        Game game = creategame();

        Ship ship = game.getPlayers().get(0).getShips().get(0);

        gameService.moveShip(ship.getShipId(), "b");

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from Ship s where s.shipId = :shipId");
        q.setParameter("shipId", ship.getShipId());
        Ship shipDb = (Ship) q.uniqueResult();
        tx.commit();

        List<Colony> colonies = shipDb.getPlayer().getColonies();
        assertEquals("The player should have 2 colonies", 2, colonies.size());
        assertEquals("The second colony the player should have is on planet b.", "b", colonies.get(1).getPlanet().getName());
    }

    @Test
    public void MoveShipAndCreateColony_PlanetAlreadyColonizedByPlayer_NoColonyPlaced() throws Exception {
        Game game = creategame();

        Ship ship = game.getPlayers().get(0).getShips().get(0);

        gameService.moveShip(ship.getShipId(), "b");

        gameService.moveShip(ship.getShipId(), "a");

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from Ship s where s.shipId = :shipId");
        q.setParameter("shipId", ship.getShipId());
        Ship shipDb = (Ship) q.uniqueResult();
        tx.commit();

        List<Colony> colonies = shipDb.getPlayer().getColonies();
        assertEquals("The player should have Only 2 colonies", 2, colonies.size());

    }

    @Test(expected = SpaceCrackNotAcceptableException.class, timeout = 20000)
    public void MoveShipWithNoCommandPoints_SpaceCrackNotAcceptableException() throws Exception {
        Game game = creategame();

        Ship ship = game.getPlayers().get(0).getShips().get(0);


            gameService.moveShip(ship.getShipId(), "b");
            gameService.moveShip(ship.getShipId(), "c");
        gameService.moveShip(ship.getShipId(), "b");
        gameService.moveShip(ship.getShipId(), "c");
        gameService.moveShip(ship.getShipId(), "b");
        gameService.moveShip(ship.getShipId(), "c");
        gameService.moveShip(ship.getShipId(), "b");
        gameService.moveShip(ship.getShipId(), "c");
        gameService.moveShip(ship.getShipId(), "b");
        gameService.moveShip(ship.getShipId(), "c");
        gameService.moveShip(ship.getShipId(), "b");
        gameService.moveShip(ship.getShipId(), "c");

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
    public void getGameFromGameId() throws Exception {
        Game expected = creategame();

        IGameRepository gameRepository = mock(IGameRepository.class);
        GameService gameService1 = new GameService(new MapService(), new PlanetRepository(), new ColonyRepository(), new ShipRepository(), new PlayerRepository(), gameRepository);
        stub(gameRepository.getGameByGameId(expected.getGameId())).toReturn(expected);

        Game actual = gameService1.getGameByGameId(expected.getGameId());

        verify(gameRepository, VerificationModeFactory.times(1)).getGameByGameId(expected.getGameId());
        assertEquals("Actual gameId should be the same as the expected gameId", expected.getGameId(), actual.getGameId());
    }

    @Test(expected = SpaceCrackNotAcceptableException.class)
    public void endPlayerTurn() throws Exception {

        Game game = creategame();
        Player player = game.getPlayers().get(0);
        int oldCommandPoints = player.getCommandPoints();
        gameService.endTurn(player.getPlayerId());
        player = playerRepository.getPlayerByPlayerId(player.getPlayerId());

        assertEquals(oldCommandPoints + GameService.COMMANDPOINTSPERTURN, player.getCommandPoints());
        gameService.moveShip(player.getShips().get(0).getShipId(), "b");
    }

    @Test
    public void endTurnBothPlayers_newCommandPoints() throws Exception {
        Game game = creategame();
        Player player1 = game.getPlayers().get(0);
        int oldCommandPointsOfPlayer1 = player1.getCommandPoints();
        gameService.endTurn(player1.getPlayerId());
        Player player2 = game.getPlayers().get(1);
        int oldCommandPointsOfPlayer2 = player2.getCommandPoints();

        gameService.endTurn(player2.getPlayerId());
        player1 = playerRepository.getPlayerByPlayerId(player1.getPlayerId());
        player2 = playerRepository.getPlayerByPlayerId(player2.getPlayerId());

        assertEquals("player1's turn shouldn't be ended", false,player1.isTurnEnded());
        assertEquals("player2's turn shouldn't be ended", false,player2.isTurnEnded());

        assertEquals(oldCommandPointsOfPlayer1 + GameService.COMMANDPOINTSPERTURN, player1.getCommandPoints());
        assertEquals(oldCommandPointsOfPlayer2 + GameService.COMMANDPOINTSPERTURN, player2.getCommandPoints());

        gameService.moveShip(player1.getShips().get(0).getShipId(), "b");
        gameService.moveShip(player2.getShips().get(0).getShipId(), "b3");
    }
}
