package be.kdg.spacecrack;


import be.kdg.spacecrack.Exceptions.UserNotFoundException;
import be.kdg.spacecrack.controllers.TokenController;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.utilities.HibernateUtil;
import be.kdg.spacecrack.utilities.ITokenStringGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;

import static junit.framework.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by Tim on 3/02/14.
 */

@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class LoginTests {

    @Autowired
    private ServletContext servletContext;
    private WebApplicationContext ctx;
    private MockMvc mockMvc;
    private TokenController tokenController;
    private User testUser;
    private ITokenStringGenerator mockTokenGenerator;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
       ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            mockMvc = webAppContextSetup(ctx).build();

        mockTokenGenerator = Mockito.mock(ITokenStringGenerator.class);
        tokenController = new TokenController(mockTokenGenerator);
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
    public void testRequestAccessToken_ValidUser_Ok()
    {

        String name ="testUsername";
        String pw = "testPassword";
        User user = new User(name, pw);
        String expectedTokenValue = "testtokenvalue1234";
        Mockito.stub(mockTokenGenerator.generateTokenString()).toReturn(expectedTokenValue);
        AccessToken token = tokenController.getToken(user);


        assertEquals("Token value should be testtokenvalue1234", expectedTokenValue, token.getValue() );
    }

    @Test(expected = UserNotFoundException.class)
    public void testRequestAccessToken_InvalidUser_UserNotFoundException()
    {

        String name ="badUser";
        String pw = "badPw";
        User user = new User(name, pw);
        tokenController.getToken(user);

    }

    @Test
    public void integrationTestLogin_ValidUser_Token() throws Exception {

        String userjson = objectMapper.writeValueAsString(testUser);
        System.out.println("Userjson : " + userjson);

        MockHttpServletRequestBuilder requestBuilder = post("/accesstokens");
        mockMvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"testUsername\",\"password\":\"testPassword\"}").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8")).andExpect(jsonPath("$.value", CoreMatchers.notNullValue()));


    }

    @Test
    public void testUserAlreadyLoggedRelogin() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post("/accesstokens");

        MvcResult firstResult = mockMvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"testUsername\",\"password\":\"testPassword\"}").accept(MediaType.APPLICATION_JSON))
                .andReturn();
        MvcResult secondResult = mockMvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"testUsername\",\"password\":\"testPassword\"}").accept(MediaType.APPLICATION_JSON))
                .andReturn();

        String expected = objectMapper.readValue(firstResult.getResponse().getContentAsString(), AccessToken.class).getValue();
        String actual = objectMapper.readValue(secondResult.getResponse().getContentAsString(), AccessToken.class).getValue();

        assertEquals("Same token should be retrieved", expected,actual);
    }

    @Test
    public void PostToAccesstokens_InvalidUser_Unauthorized() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post("/accesstokens").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"badUser\",\"password\":\"testPassword\"}").accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isUnauthorized());

    }

    /*@Test
    public void RequestToServer_ExpiredToken_Unauthorized() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get("/hello").header("token","testtokenvalue1234");
        mockMvc.perform(requestBuilder).andExpect(status().isUnauthorized());
    }*/

    @After
    public void tearDown() throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();

     session.delete(testUser);
        tx.commit();


    }
}
