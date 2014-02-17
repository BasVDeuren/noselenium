package be.kdg.spacecrack.integrationtests;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.springframework.http.MediaType;

import javax.servlet.http.Cookie;

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
            .andExpect(jsonPath("$.player1.colonies[0].planet.name", CoreMatchers.is("a")))
            .andExpect(jsonPath("$.player1.ships[0].planet.name", CoreMatchers.is("a")));
    }


}
