package be.kdg.spacecrack.unittests;

import be.kdg.spacecrack.model.Contact;
import be.kdg.spacecrack.model.Game;
import be.kdg.spacecrack.model.Player;
import be.kdg.spacecrack.model.SpaceCrackMap;
import be.kdg.spacecrack.services.GameService;
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

    @Test
    public void createGame_validParams_gameCreated() throws Exception {
        GameService gameService = new GameService();
        Contact creator = new Contact();
        Contact opponent = new Contact();

        Game game = gameService.createGame(creator, opponent);
        List<Player> players = game.getPlayers();
        assertEquals("size() of players should be 2", 2, players.size());
        Player creatorPlayer = players.get(0);
        assertTrue(creatorPlayer.getContact() == creator);
        Player opponentplayer = players.get(1);
        assertTrue(opponentplayer.getContact() == opponent);
        SpaceCrackMap map = game.getSpaceCrackMap();
        assertEquals(map.getPlanets()[0].getPlayer(), creatorPlayer);
    }
}
