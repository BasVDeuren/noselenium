package be.kdg.spacecrack.integrationtests;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.viewmodels.ProfileWrapper;
import be.kdg.spacecrack.viewmodels.UserViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//import org.codehaus.jackson.map.ObjectMapper;

public class IntegrationProfileTests extends BaseFilteredIntegrationTests {
    public static final String USERNAME = "profiletestusername";


    AccessToken accessToken;

    @Before
    public void setUp() throws Exception {
        String UserWrapper = objectMapper.writeValueAsString(new UserViewModel(USERNAME, "password", "password", "email@gmail.com"));
        MockHttpServletRequestBuilder postRequestBuilder = post("/user");
        String stringAccessToken = mockMvc.perform(postRequestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(UserWrapper)).andReturn().getResponse().getContentAsString();

        accessToken = objectMapper.readValue(stringAccessToken, AccessToken.class);
    }

    @Test
    public void testUpdateEditProfile_ValidProfile_statusOk() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        ProfileWrapper profile = new ProfileWrapper("firstname", "lastname", "email", "12-01-2013", "image");

        String profileJson = objectMapper.writeValueAsString(profile);

        MockHttpServletRequestBuilder postRequestBuilder = post("/auth/profile");
        mockMvc.perform(postRequestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .content(profileJson)
                .cookie(new Cookie("accessToken", "%22" + accessToken.getValue() + "%22"))).andExpect(status().isOk());
    }


    @After
    public void tearDown() throws Exception {
        Session session = sessionFactory.getCurrentSession();


        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("delete from User u where u.username= :name  ");
        q.setParameter("name", USERNAME);
        q.executeUpdate();

    }
}
