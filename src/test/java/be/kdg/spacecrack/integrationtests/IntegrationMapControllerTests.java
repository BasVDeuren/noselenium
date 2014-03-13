package be.kdg.spacecrack.integrationtests;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

public class IntegrationMapControllerTests extends BaseFilteredIntegrationTests {
    @Test
    public void getMap_validRequest_mapWithPlanets() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/map").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.planets[0].x", CoreMatchers.is(100)))
                .andExpect(jsonPath("$.planets[0].y", CoreMatchers.is(500)))
               /* .andExpect(jsonPath("$.planets[0].connectedPlanets[0].name", CoreMatchers.notNullValue()))*/.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        boolean b = true;
    }
}
