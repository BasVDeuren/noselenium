package be.kdg.spacecrack.integrationtests;


import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.TokenRepository;
import be.kdg.spacecrack.repositories.UserRepository;
import be.kdg.spacecrack.utilities.HibernateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.servlet.http.Cookie;

import static junit.framework.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//import org.codehaus.jackson.map.ObjectMapper;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
public class IntegrationAccessTokenControllerTests extends BaseFilteredIntegrationTests {
    private User testUser;
    private ObjectMapper objectMapper;


    @Before
    public void setUp() throws Exception {


        //mockTokenGenerator = mock(ITokenStringGenerator.class);

        objectMapper = new ObjectMapper();

        Session session= HibernateUtil.getSessionFactory().getCurrentSession();
        try {

            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                testUser = new User("testUsername", "testPassword", "testEmail@gmail.com");
                session.saveOrUpdate(testUser);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw new RuntimeException(e);
            }
        } finally {
            HibernateUtil.close(session);
        }

    }

  /* @Test
    public void testLoginIntegrated() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/tokens").param("username", "testUser").param("password", "testPassword")).andExpect(jsonPath("token", notNullValue()));
    }*/


    @Test
    public void login_ValidUser_Token() throws Exception {

        String userjson = objectMapper.writeValueAsString(testUser);

        MockHttpServletRequestBuilder requestBuilder = post("/accesstokens");
        mockMvc.perform(requestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .content(userjson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.value", CoreMatchers.notNullValue()));


    }

    @Test
    public void login_SameValidUserTwice_SameToken() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post("/accesstokens");

        MvcResult firstResult = mockMvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"testEmail@gmail.com\",\"password\":\"testPassword\"}").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        MvcResult secondResult = mockMvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"testEmail@gmail.com\",\"password\":\"testPassword\"}").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String expected = objectMapper.readValue(firstResult.getResponse().getContentAsString(), AccessToken.class).getValue();
        String actual = objectMapper.readValue(secondResult.getResponse().getContentAsString(), AccessToken.class).getValue();

        assertEquals("Same token should be retrieved", expected, actual);
    }

    @Test
    public void login_InvalidUser_Unauthorized() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = post("/accesstokens").contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"badUser\",\"password\":\"testPassword\"}").accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isUnauthorized());

    }


    @Test
    public void logout_validToken_nolongerauthorized() throws Exception {
        String userjson = objectMapper.writeValueAsString(testUser);

        MockHttpServletRequestBuilder requestBuilder = post("/accesstokens");
        mockMvc.perform(requestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .content(userjson)
                .accept(MediaType.APPLICATION_JSON))  ;
        UserRepository userRepository = new UserRepository();
        TokenRepository tokenRepository = new TokenRepository();

        AccessToken accessToken = tokenRepository.getAccessTokenByValue(userRepository.getUserByUsername(testUser.getUsername()).getToken().getValue());

        tokenRepository.deleteAccessToken(accessToken);

        MockHttpServletRequestBuilder logoutRequestBuilder = delete("/accesstokens");
        mockMvc.perform(logoutRequestBuilder
            .cookie(new Cookie("accessToken", "\"" + accessToken.getValue() + "\"")))
        .andExpect(status().isBadRequest());


    }


    @Test
    public void logout_invalidtoken_ok() throws Exception {
        MockHttpServletRequestBuilder deleteRequestBuilder = delete("/accesstokens")
                .cookie(new Cookie("accessToken", "\"" + new AccessToken("invalid").getValue() + "\""));
        mockMvc.perform(deleteRequestBuilder).andExpect(status().isBadRequest());
    }

    @Test
    public void checkIfLoggedIn_validAccessToken_StatusOk() throws Exception {
        String accessTokenValue = loginAndRetrieveAccessToken();

        mockMvc.perform(get("/auth/accesstokens")
                .cookie(new Cookie("accessToken", accessTokenValue))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void checkIfLoggedIn_noAccessToken_SpaceCrackUnauthorizedException() throws Exception {
        MockHttpServletRequestBuilder getAccessTokenIsUserLoggedIn = get("/auth/accesstokens")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(getAccessTokenIsUserLoggedIn).andExpect(status().isUnauthorized());
    }

    @Test
    public void checkIfLoggedIn_invalidAccessToken_SpaceCrackUnauthorizedException() throws Exception {
        MockHttpServletRequestBuilder getAccessTokenIsUserLoggedIn = get("/auth/accesstokens")
                .accept(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("accessToken", "blablabla"));

        mockMvc.perform(getAccessTokenIsUserLoggedIn).andExpect(status().isUnauthorized());
    }

    @After
    public void tearDown() throws Exception {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        try {
            Transaction   tx = session.beginTransaction();
            try {

                @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("delete from User");
                q.executeUpdate();
                tx.commit();
            } catch (RuntimeException e) {
                tx.rollback();
                throw e;
            }
        } finally {
            HibernateUtil.close(session);
        }

    }
}
