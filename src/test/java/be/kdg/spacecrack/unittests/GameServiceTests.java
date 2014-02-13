package be.kdg.spacecrack.unittests;

import be.kdg.spacecrack.model.Contact;
import be.kdg.spacecrack.model.Game;
import be.kdg.spacecrack.services.GameService;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Tim on 13/02/14.
 */
public class GameServiceTests {

    @Test
    public void createGame_validParams_gameCreated() throws Exception {
        GameService gameService = new GameService();
        Contact creator = new Contact();
        Contact opponent = new Contact();

        Game game = gameService.createGame(creator, opponent);
        Set<Contact> contacts = game.getPlayers();
        assertEquals("size() of contacts should be 2", 2 ,contacts.size());
        assertTrue(contacts.contains(creator));
        assertTrue(contacts.contains(opponent));



    }
}
