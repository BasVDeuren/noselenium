package be.kdg.spacecrack.integrationtests;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.jsonviewmodels.ActionViewModel;
import be.kdg.spacecrack.model.Game;
import be.kdg.spacecrack.model.Ship;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.springframework.http.MediaType;

import javax.servlet.http.Cookie;

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

        ActionViewModel moveShipActionViewModel = new ActionViewModel("moveShip", ship, destinationPlanet, null);

        String moveShipActionJson = objectMapper.writeValueAsString(moveShipActionViewModel);

        mockMvc.perform(post("/auth/action")
                .contentType(MediaType.APPLICATION_JSON)
                .content(moveShipActionJson)
                .cookie(new Cookie("accessToken", accessToken)))
                .andExpect(status().isOk());
    }


    @Test
    public void getAllGamesByProfile() throws Exception {
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
                .andExpect(jsonPath("$.[0].gameId", CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.[0].name", CoreMatchers.notNullValue()));
    }

    @Test
    public void getGameByGameId() throws Exception {
        String accessToken = login();
        String gameJson = mockMvc.perform(post("/auth/game")
                .accept(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("accessToken", accessToken))).andReturn().getResponse().getContentAsString();

        Game expected = objectMapper.readValue(gameJson, Game.class);

        mockMvc.perform(get("/auth/game/specificGame/" + expected.getGameId())
                .accept(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("accessToken", accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId", CoreMatchers.notNullValue()));
    }

    @Test
    public void endPlayerTurn() throws Exception {
        String accessToken = login();
        String gameJson = mockMvc.perform(post("/auth/game")
                .accept(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("accessToken", accessToken))).andReturn().getResponse().getContentAsString();

        Game game = objectMapper.readValue(gameJson, Game.class);

        mockMvc.perform(post("/auth/action")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ActionViewModel("ENDTURN", game.getPlayer1().getShips().get(0), "", game.getPlayer1().getPlayerId())))
                .cookie(new Cookie("accessToken", accessToken))
        ).andExpect(status().isOk());
    }
}
