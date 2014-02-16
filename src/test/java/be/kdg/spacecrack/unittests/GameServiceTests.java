package be.kdg.spacecrack.unittests;

import be.kdg.spacecrack.model.*;
import be.kdg.spacecrack.services.GameService;
import be.kdg.spacecrack.services.MapService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
public class GameServiceTests {

    private GameService gameService;

    @Before
    public void setUp() throws Exception {
        gameService = new GameService(new MapService());

    }

    @Test
    public void createGame_validParams_gameCreated() throws Exception {

        Contact creator = new Contact();
        Contact opponent = new Contact();

        Game game = gameService.createGame(creator, opponent);
        List<Player> players = game.getPlayers();
        assertEquals("size() of players should be 2", 2, players.size());
        Player player1 = players.get(0);
        assertTrue(player1.getContact() == creator);
        Player player2 = players.get(1);
        assertTrue(player2.getContact() == opponent);
        SpaceCrackMap map = game.getSpaceCrackMap();
        Planet player1StartingPlanet = map.getPlayer1StartingPlanet();
        Planet player2StartingPlanet = map.getPlayer2StartingPlanet();

        assertEquals(player1, player1StartingPlanet.getPlayer());

        assertEquals(player2, player2StartingPlanet.getPlayer());

        assertTrue(player1.getShips().size() == 1);
        assertTrue(player2.getShips().size() == 1);
     /* Ship player1StartingPlanetShip = player1StartingPlanet.getShip();
        assertTrue("player 1 should have a starting ship", player1.getShips().contains(player1StartingPlanetShip));
        Ship player2StartingPlanetShip = player2StartingPlanet.getShip();
        assertTrue("player 2 should have a starting ship", player2getShips().contains(player2StartingPlanetShip));*/
    }



//    @Test
//    public void moveShip_connectedPlanet_shipMoved()
//    {
//        Contact creator = new Contact();
//        Contact opponent = new Contact();
//
//
//        assertEquals(ship.getPlanet(), planetA);
//        gameService.moveShip(creatorPlayer, ship,planetB);
//        assertEquals(ship.getPlanet(), planetB);
//    }

/*    @Test(expected = SpaceCrackNotAcceptableException.class)
    public void moveShip_PlanetNotConnected_notAcceptable()
    {
        Contact creator = new Contact();
        Contact opponent = new Contact();
        Game game = gameService.createGame(creator, opponent);
        Player creatorPlayer = game.getPlayers().get(0);

        Planet planetA = game.getSpaceCrackMap().getPlanets()[0];
        Planet planetC = game.getSpaceCrackMap().getPlanets()[3];

        assertEquals(ship.getPlanet(), planetA);
        gameService.moveShip(creatorPlayer, ship,planetC);
        assertEquals(ship.getPlanet(), planetC);
    }

    @Test(expected = SpaceCrackNotAcceptableException.class)
    public void moveShip_invalidPlayer_NotAcceptable() throws Exception {
        Contact creator = new Contact();
        Contact opponent = new Contact();
        Game game = gameService.createGame(creator, opponent);
        Player creatorPlayer = game.getPlayers().get(0);
        Player opponentPlayer = game.getPlayers().get(1);
        Planet planetA = game.getSpaceCrackMap().getPlanets()[0];
        Planet planetB = game.getSpaceCrackMap().getPlanets()[1]; 
        gameService.moveShip(creatorPlayer, ship, planetB);
    }*/

}
