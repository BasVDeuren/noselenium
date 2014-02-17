package be.kdg.spacecrack.unittests;

import be.kdg.spacecrack.model.Game;
import be.kdg.spacecrack.model.Player;
import be.kdg.spacecrack.model.Profile;
import be.kdg.spacecrack.repositories.PlanetRepository;
import be.kdg.spacecrack.services.GameService;
import be.kdg.spacecrack.services.MapService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

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

        gameService = new GameService(new MapService(),new PlanetRepository() );

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

    @Test
    public void testMoveShip() throws Exception {

    }
}
