package be.kdg.spacecrack.integrationtests;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.controllers.GameController;
import be.kdg.spacecrack.jsonviewmodels.Action;
import be.kdg.spacecrack.model.Game;
import be.kdg.spacecrack.model.Ship;
import be.kdg.spacecrack.repositories.*;
import be.kdg.spacecrack.services.AuthorizationService;
import be.kdg.spacecrack.services.GameService;
import be.kdg.spacecrack.services.MapService;
import be.kdg.spacecrack.utilities.TokenStringGenerator;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.springframework.http.MediaType;

import javax.servlet.http.Cookie;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IntegrationGameControllerTests extends BaseFilteredIntegrationTests {
    @Test
    public void testCreateGame_AuthorisedUser_Map() throws Exception {
        String accessToken = login();
        mockMvc.perform(post("/auth/game")
                .accept(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("accessToken", accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.player1.colonies[0].planetName", CoreMatchers.is("a")))
                .andExpect(jsonPath("$.player1.ships[0].planetName", CoreMatchers.is("a")))
                .andExpect(jsonPath("$.player1.ships[0].shipId", CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.player1.ships[0].shipId", CoreMatchers.not(0)));
    }

    @Test
    public void MoveShip_validPlanet_Ok() throws Exception {
        String accessToken = login();
        String gameJson = mockMvc.perform(post("/auth/game")
                .accept(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("accessToken", accessToken))).andReturn().getResponse().getContentAsString();
        System.out.println(gameJson);
        Game game = objectMapper.readValue(gameJson, Game.class);

        Ship ship = game.getPlayer1().getShips().get(0);
        String destinationPlanet = "b";

        Action moveShipAction = new Action("moveShip", ship, destinationPlanet);

        String moveShipActionJson = objectMapper.writeValueAsString(moveShipAction);

        mockMvc.perform(post("/auth/action")
                .contentType(MediaType.APPLICATION_JSON)
                .content(moveShipActionJson)
                .cookie(new Cookie("accessToken", accessToken)))
                .andExpect(status().isOk());
    }

    @Test
    public void GetGames_LoggedInUser_GamesFromUser() throws Exception {
        String accessToken = login();

        mockMvc.perform(post("/auth/game")
                .accept(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("accessToken", accessToken)));

        mockMvc.perform(post("/auth/game")
                .accept(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("accessToken", accessToken)));

        mockMvc.perform(get("/auth/game")
                .accept(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("accessToken", accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gameId", CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$[1].gameId", CoreMatchers.notNullValue()));

    }


    @Test
    public void testName() throws Exception {
        String accessToken = login();
        accessToken = accessToken.substring(3, accessToken.length() -3);
        GameController gameController = new GameController(new AuthorizationService(new TokenRepository(), new UserRepository(), new TokenStringGenerator()), new GameService(new MapService(), new PlanetRepository(), new ColonyRepository(), new ShipRepository(), new PlayerRepository(), new GameRepository()));
        gameController.createGame(accessToken);
        gameController.createGame(accessToken);
        List<Game> games = gameController.getGamesByAccessToken(accessToken);
        String s = objectMapper.writeValueAsString(games);
        boolean b = true;

    }
}


