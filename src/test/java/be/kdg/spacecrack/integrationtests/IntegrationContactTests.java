package be.kdg.spacecrack.integrationtests;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.jsonviewmodels.ContactWrapper;
import be.kdg.spacecrack.jsonviewmodels.UserWrapper;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IntegrationContactTests extends BaseFilteredIntegrationTests{
    User testUser;
    ObjectMapper objectMapper = new ObjectMapper();
    AccessToken accessToken = new AccessToken();

    @Before
    public void setUp() throws Exception {
        String UserWrapper = objectMapper.writeValueAsString(new UserWrapper("username", "password", "password", "email"));
        MockHttpServletRequestBuilder postRequestBuilder = post("/user");
        String stringAccessToken = mockMvc.perform(postRequestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(UserWrapper)).andReturn().getResponse().getContentAsString();
        String[] accessTokenValues = stringAccessToken.split(":");
        String[] accesstokenId = accessTokenValues[1].split(",");
        accessToken.setAccessTokenId(Integer.parseInt(accesstokenId[0]));
        accessToken.setValue(accessTokenValues[2].substring(1, accessTokenValues[2].length()-2));
    }

    @Test
    public void testPostAddContact_ValidContact_StatusOk() throws Exception {
     /*     MockHttpServletRequestBuilder getRequestBuilder = get("/user/auth")
                .accept(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("accessTokenvalue", accessToken.getValue()));

        String stringUser = mockMvc.perform(getRequestBuilder).andReturn().getResponse().getContentAsString();
        String[] userPartvalues = stringUser.split(":");

      String valuesToUse = "";
        for(String s : userPartvalues){
            valuesToUse += s.split(",")[0] + ",";
        }*/

//        String[] uservalues = valuesToUse.split(",");

//        User user = new User(uservalues[2], uservalues[3], uservalues[4]);

        ObjectMapper objectMapper = new ObjectMapper();

        ContactWrapper contact = new ContactWrapper("firstname","lastname","email","12-01-2013","image");

        String contactJson = objectMapper.writeValueAsString(contact);

        MockHttpServletRequestBuilder postRequestBuilder = post("/auth/contact");
        mockMvc.perform(postRequestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .content(contactJson)
                .cookie(new Cookie("accessToken","%22"+ accessToken.getValue() + "%22"))).andExpect(status().isOk());
    }
}
