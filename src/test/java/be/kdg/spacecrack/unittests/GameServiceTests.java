package be.kdg.spacecrack.unittests;

import be.kdg.spacecrack.Exceptions.SpaceCrackNotAcceptableException;
import be.kdg.spacecrack.model.*;
import be.kdg.spacecrack.repositories.*;
import be.kdg.spacecrack.services.GameService;
import be.kdg.spacecrack.services.IColonyRepository;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.transaction.annotation.Transactional;

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
public class GameServiceTests extends BaseUnitTest{

    private GameService gameService;
    private User user;
    private IPlayerRepository playerRepository;
    private Profile opponentProfile;


    @Before
    public void setUp() throws Exception {
        playerRepository = new PlayerRepository(sessionFactory);
        gameService = new GameService(new PlanetRepository(sessionFactory), new ColonyRepository(sessionFactory), new ShipRepository(sessionFactory), playerRepository, new GameRepository(sessionFactory));
        user = new User();
        Profile profile = new Profile();
        opponentProfile = new Profile();
        User opponentUser = new User();
        opponentUser.setProfile(opponentProfile);
        user.setProfile(profile);
    }



    private Game creategame() {
        int gameId = gameService.createGame(user.getProfile(), "SpaceCrackName", opponentProfile);
        Game game = gameService.getGameByGameId(gameId);

        return game;
    }

    @Transactional @Test
    public void moveShipAndBuildColony_validPlanet_shipmovedandColonyBuilt() throws Exception {
        Game game = creategame();

        Player player = game.getPlayers().get(0);
        int oldCommandPoints = player.getCommandPoints();
        Ship ship = player.getShips().get(0);

        gameService.moveShip(ship.getShipId(), "b");
        Player playerDb = playerRepository.getPlayerByPlayerId(player.getPlayerId());
        Planet shipLocation = gameService.getShipLocationByShipId(ship.getShipId());

        assertEquals("b", shipLocation.getName());
        assertEquals("Player should have lost", oldCommandPoints - GameService.MOVESHIPCOST - GameService.CREATECOLONYCOST, playerDb.getCommandPoints());

    }

    @Transactional @Test(expected = SpaceCrackNotAcceptableException.class)
    public void moveShip_invalidPlanet_shipNotMoved() throws Exception {
        Game game = creategame();

        Ship ship = game.getPlayers().get(0).getShips().get(0);
        gameService.moveShip(ship.getShipId(), "f");
        Planet shipLocation = gameService.getShipLocationByShipId(ship.getShipId());

        assertEquals("a", shipLocation.getName());

    }

    @Transactional @Test
    public void moveShipAndCreateColony_validPlanetNoColonyOnPlanet_ColonyPlaced() throws Exception {
        Game game = creategame();

        Ship ship = game.getPlayers().get(0).getShips().get(0);

        gameService.moveShip(ship.getShipId(), "b");

        Session session = sessionFactory.getCurrentSession();

        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from Ship s where s.shipId = :shipId");
        q.setParameter("shipId", ship.getShipId());
        Ship shipDb = (Ship) q.uniqueResult();


        List<Colony> colonies = shipDb.getPlayer().getColonies();
        assertEquals("The player should have 2 colonies", 2, colonies.size());
        assertEquals("The second colony the player should have is on planet b.", "b", colonies.get(1).getPlanet().getName());
    }

    @Transactional @Test
    public void moveShipAndCreateColony_PlanetAlreadyColonizedByPlayer_NoColonyPlaced() throws Exception {
        Game game = creategame();

        Ship ship = game.getPlayers().get(0).getShips().get(0);

        gameService.moveShip(ship.getShipId(), "b");

        gameService.moveShip(ship.getShipId(), "a");

        Session session = sessionFactory.getCurrentSession();

        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("from Ship s where s.shipId = :shipId");
        q.setParameter("shipId", ship.getShipId());
        Ship shipDb = (Ship) q.uniqueResult();


        List<Colony> colonies = shipDb.getPlayer().getColonies();
        assertEquals("The player should have Only 2 colonies", 2, colonies.size());

    }


    @Transactional @Test
    public void moveShip_PlanetHasShip_ShipsMerged() throws Exception {

        int argShipId = 1;
        String argDestinationPlanetName = "b";

        IShipRepository mockShipRepository = mock(IShipRepository.class);
        IColonyRepository mockColonyRepository = mock(IColonyRepository.class);
        IPlayerRepository mockPlayerRepository = mock(IPlayerRepository.class);

        // IMapFactory mapService = mock(IMapFactory.class);
        Planet planetA = new Planet();
        planetA.setName("a");
        planetA.setPlanetId(1);
        Planet planetB = new Planet();
        planetB.setName("b");
        planetB.setPlanetId(2);

        planetA.addConnection(new PlanetConnection(planetA, planetB));
        planetB.addConnection(new PlanetConnection(planetB, planetA));

        Planet[] planets = new Planet[2];
        planets[0] = planetA;
        planets[1] = planetB;

        Player player = new Player();
        player.setTurnEnded(false);
        player.setPlayerId(1);
        player.setCommandPoints(10);

        Colony colonyA = new Colony();
        colonyA.setPlanet(planetA);
        colonyA.setPlayer(player);
        Colony colonyB = new Colony();
        colonyB.setPlanet(planetB);
        colonyB.setPlayer(player);
        List<Colony> colonies = new ArrayList<Colony>();
        colonies.add(colonyA);
        colonies.add(colonyB);
        stub(mockColonyRepository.getColoniesByGame(any(Game.class))).toReturn(colonies);

        Ship ship = new Ship();
        ship.setPlanet(planets[0]);
        ship.setPlayer(player);
        Ship shipOnDestinationPlanet = new Ship();
        shipOnDestinationPlanet.setPlanet(planets[1]);
        shipOnDestinationPlanet.setPlayer(player);
        List<Ship> playerShips = new ArrayList<Ship>();
        playerShips.add(ship);
        playerShips.add(shipOnDestinationPlanet);
        player.setShips(playerShips);
        stub(mockShipRepository.getShipByShipId(argShipId)).toReturn(ship);

        GameService gameServiceWithMockedDependencies = new GameService(null, mockColonyRepository, mockShipRepository, mockPlayerRepository, null );

        gameServiceWithMockedDependencies.moveShip(argShipId, argDestinationPlanetName);

        Mockito.verify(mockShipRepository, VerificationModeFactory.times(1)).deleteShip(ship);
        // SpaceCrackMap simpleTestMap = new SpaceCrackMap(planets);
        // stub(mapService.getSpaceCrackMap()).toReturn(simpleTestMap);


    }


    @Transactional @Test(expected = SpaceCrackNotAcceptableException.class, timeout = 20000)
    public void moveShip_NoCommandPoints_SpaceCrackNotAcceptableException() throws Exception {
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

    @Transactional @Test
    public void getAllGamesFromPlayer_validPlayer_gamesRetrieved() throws Exception {
        Game game = creategame();

        IGameRepository gameRepository = mock(IGameRepository.class);
        ArrayList<Game> expected = new ArrayList<Game>();
        expected.add(new Game());
        expected.add(new Game());
        stub(gameRepository.getGamesByProfile(user.getProfile())).toReturn(expected);
        GameService gameService1 = new GameService(new PlanetRepository(), new ColonyRepository(), new ShipRepository(), new PlayerRepository(), gameRepository);

        List<Game> actual = gameService1.getGames(user);

        verify(gameRepository, VerificationModeFactory.times(1)).getGamesByProfile(user.getProfile());
        assertEquals(expected, actual);
    }


    @Test
    @Transactional
    public void getGameFromGameId() throws Exception {
        Game expected = creategame();

        IGameRepository gameRepository = mock(IGameRepository.class);
        GameService gameService1 = new GameService(new PlanetRepository(), new ColonyRepository(), new ShipRepository(), new PlayerRepository(), gameRepository);
        stub(gameRepository.getGameByGameId(expected.getGameId())).toReturn(expected);

        Game actual = gameService1.getGameByGameId(expected.getGameId());

        verify(gameRepository, VerificationModeFactory.times(1)).getGameByGameId(expected.getGameId());
        assertEquals("Actual gameId should be the same as the expected gameId", expected.getGameId(), actual.getGameId());
    }

    @Transactional @Test(expected = SpaceCrackNotAcceptableException.class)
    public void endPlayerTurn() throws Exception {

        Game game = creategame();
        Player player = game.getPlayers().get(0);
        int oldCommandPoints = player.getCommandPoints();
        gameService.endTurn(player.getPlayerId());
        player = playerRepository.getPlayerByPlayerId(player.getPlayerId());

        assertEquals(oldCommandPoints + GameService.COMMANDPOINTSPERTURN, player.getCommandPoints());
        gameService.moveShip(player.getShips().get(0).getShipId(), "b");
    }

    @Transactional @Test
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

        assertEquals("player1's turn shouldn't be ended", false, player1.isTurnEnded());
        assertEquals("player2's turn shouldn't be ended", false, player2.isTurnEnded());

        assertEquals(oldCommandPointsOfPlayer1 + GameService.COMMANDPOINTSPERTURN, player1.getCommandPoints());
        assertEquals(oldCommandPointsOfPlayer2 + GameService.COMMANDPOINTSPERTURN, player2.getCommandPoints());

        gameService.moveShip(player1.getShips().get(0).getShipId(), "b");
        gameService.moveShip(player2.getShips().get(0).getShipId(), "b3");
    }

    @Transactional @Test
    public void buildShip_NoShipOnPlanetEnoughCommandPoints_shipBuilt() {
        Game game = creategame();
        Player player = game.getPlayers().get(0);
        Ship ship = player.getShips().get(0);
        int oldAmountOfShips = player.getShips().size();

        gameService.moveShip(ship.getShipId(), "b");
        Player playerDb = playerRepository.getPlayerByPlayerId(player.getPlayerId());
        int oldCommandPoints = playerDb.getCommandPoints();
        Colony colony = player.getColonies().get(0);
        gameService.buildShip(colony.getColonyId());
        playerDb = playerRepository.getPlayerByPlayerId(player.getPlayerId());
        List<Ship> playerDbShips = playerDb.getShips();
        Ship newShip = playerDbShips.get(1);
        assertEquals("Player should have 1 more ship", oldAmountOfShips + 1, playerDbShips.size());

        assertEquals("The ship should have strength", GameService.NEWSHIPSTRENGTH,  newShip.getStrength());
        assertEquals("Ship should be build on colony's planet", colony.getPlanet().getName(), playerDbShips.get(playerDbShips.size() - 1).getPlanet().getName());
        assertEquals("Player should have lost 3 commandPoints", oldCommandPoints - GameService.BUILDSHIPCOST, playerDb.getCommandPoints());

    }

    @Transactional @Test(expected = SpaceCrackNotAcceptableException.class)
    public void buildShip_NoShipOnPlanetNotEnoughCommandPoints_NoShipBuilt() {
        Game game = creategame();
        Player player = game.getPlayers().get(0);
        Ship ship = player.getShips().get(0);
        gameService.moveShip(ship.getShipId(), "b");
        gameService.moveShip(ship.getShipId(), "c");
        Colony colony = player.getColonies().get(0);
        gameService.buildShip(colony.getColonyId());
    }

    @Transactional @Test
    public void buildShip_ShipOnPlanetEnoughCommandPoints_shipMerged() {
        Game game = creategame();
        Player player = game.getPlayers().get(0);
        Ship ship = player.getShips().get(0);
        int oldShipStrength = ship.getStrength();
        int oldAmountOfShips = player.getShips().size();


        Player playerDb = playerRepository.getPlayerByPlayerId(player.getPlayerId());
        int oldCommandPoints = playerDb.getCommandPoints();
        Colony colony = player.getColonies().get(0);
        gameService.buildShip(colony.getColonyId());
        playerDb = playerRepository.getPlayerByPlayerId(player.getPlayerId());
        Ship shipDb = playerDb.getShips().get(0);
        List<Ship> playerDbShips = playerDb.getShips();


        assertEquals("Player shouldn't have more ships than before", oldAmountOfShips, playerDbShips.size());
        assertEquals("Ship should be build on colony's planet", colony.getPlanet().getName(), playerDbShips.get(playerDbShips.size() - 1).getPlanet().getName());
        assertEquals("The ship standing on the planet should now be more powerful", oldShipStrength + GameService.NEWSHIPSTRENGTH, shipDb.getStrength());
        assertEquals("Player should have lost 3 commandPoints", oldCommandPoints - GameService.BUILDSHIPCOST, playerDb.getCommandPoints());

    }


}
