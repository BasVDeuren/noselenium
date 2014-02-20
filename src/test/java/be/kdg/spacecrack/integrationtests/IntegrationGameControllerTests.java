package be.kdg.spacecrack.integrationtests;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.jsonviewmodels.Action;
import be.kdg.spacecrack.model.Game;
import be.kdg.spacecrack.model.Ship;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.springframework.http.MediaType;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
}
