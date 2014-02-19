package be.kdg.spacecrack.integrationtests;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.jsonviewmodels.ProfileWrapper;
import be.kdg.spacecrack.jsonviewmodels.UserWrapper;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IntegrationProfileTests extends BaseFilteredIntegrationTests{
    public static final String USERNAME = "profiletestusername";
    User testUser;

    AccessToken accessToken;

    @Before
    public void setUp() throws Exception {
        String UserWrapper = objectMapper.writeValueAsString(new UserWrapper(USERNAME, "password", "password", "email"));
        MockHttpServletRequestBuilder postRequestBuilder = post("/user");
        String stringAccessToken = mockMvc.perform(postRequestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(UserWrapper)).andReturn().getResponse().getContentAsString();

        accessToken = objectMapper.readValue(stringAccessToken, AccessToken.class);
    }

    @Test
    public void testPostAddProfile_ValidProfile_StatusOk() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        ProfileWrapper profile = new ProfileWrapper("firstname","lastname","email","12-01-2013","image");

        String contactJson = objectMapper.writeValueAsString(profile);

        MockHttpServletRequestBuilder postRequestBuilder = post("/auth/profile");
        mockMvc.perform(postRequestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .content(contactJson)
                .cookie(new Cookie("accessToken","%22"+ accessToken.getValue() + "%22")))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateEditProfile_ValidProfile_statusOk() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        ProfileWrapper profile = new ProfileWrapper("firstname","lastname","email","12-01-2013","image");

        String profileJson = objectMapper.writeValueAsString(profile);

        MockHttpServletRequestBuilder postRequestBuilder = post("/auth/profile");
        mockMvc.perform(postRequestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .content(profileJson)
                .cookie(new Cookie("accessToken", "%22" + accessToken.getValue() + "%22"))).andReturn();

        profile.setFirstname("newFirstname");
        profileJson = objectMapper.writeValueAsString(profile);
        mockMvc.perform(post("/auth/profile/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content(profileJson)
            .cookie(new Cookie("accessToken","%22"+accessToken.getValue()+"%22")))
            .andExpect(status().isOk());
    }

    @After
    public void tearDown() throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();

        @SuppressWarnings("JpaQlInsepction") Query q = session.createQuery("delete from User u where u.username= :name  ");
        q.setParameter("name", USERNAME );
        q.executeUpdate();
        tx.commit();
    }
}
