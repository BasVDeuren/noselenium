package be.kdg.spacecrack.integrationtests;

import be.kdg.spacecrack.modelwrapper.UserWrapper;
import org.codehaus.jackson.map.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Ikke on 6-2-14.
 */
public class IntegrationRegisterTests extends BaseFilteredIntegrationTests {

    @Test
    public void testRegisterUser_NewUser_Token() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String validUserWrapperJson = objectMapper.writeValueAsString(new UserWrapper("username", "password", "password", "email"));
        MockHttpServletRequestBuilder postRequestBuilder = post("/user");
        mockMvc.perform(postRequestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(validUserWrapperJson))
                .andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.value", CoreMatchers.notNullValue()));
    }

    @Test
    public void testRegisterUser_Badrepeat_NotAcceptable() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String invalidUserWrapper = objectMapper.writeValueAsString(new UserWrapper("username", "password", "badpassword", "email"));
        MockHttpServletRequestBuilder postRequestBuilder = post("/user");
        mockMvc.perform(postRequestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(invalidUserWrapper))
                .andExpect(status().isNotAcceptable());


    }
}
