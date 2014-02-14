package be.kdg.spacecrack.integrationtests;


import be.kdg.spacecrack.controllers.TokenController;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.TokenRepository;
import be.kdg.spacecrack.repositories.UserRepository;
import be.kdg.spacecrack.utilities.HibernateUtil;
import be.kdg.spacecrack.utilities.ITokenStringGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.servlet.http.Cookie;

import static junit.framework.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
public class IntegrationAccessTokenControllerTests extends BaseFilteredIntegrationTests {


    private TokenController tokenControllerWithMockedGenerator;
    private User testUser;
    private ITokenStringGenerator mockTokenGenerator;
    private ObjectMapper objectMapper;


    @Before
    public void setUp() throws Exception {


        mockTokenGenerator = Mockito.mock(ITokenStringGenerator.class);

        objectMapper = new ObjectMapper();

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        testUser = new User("testUsername", "testPassword");
        session.saveOrUpdate(testUser);
        tx.commit();

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

        MvcResult firstResult = mockMvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"testUsername\",\"password\":\"testPassword\"}").accept(MediaType.APPLICATION_JSON))
                .andReturn();
        MvcResult secondResult = mockMvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"testUsername\",\"password\":\"testPassword\"}").accept(MediaType.APPLICATION_JSON))
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

        userRepository.DeleteAccessToken(accessToken);

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

    @After
    public void tearDown() throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("delete from User");
        q.executeUpdate();
        tx.commit();
        HibernateUtil.close(session);
    }
}
