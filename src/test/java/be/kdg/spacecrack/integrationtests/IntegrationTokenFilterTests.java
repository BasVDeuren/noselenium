package be.kdg.spacecrack.integrationtests;

import be.kdg.spacecrack.controllers.TokenController;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.TokenRepository;
import be.kdg.spacecrack.repositories.UserRepository;
import be.kdg.spacecrack.utilities.HibernateUtil;
import be.kdg.spacecrack.utilities.TokenStringGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
public class IntegrationTokenFilterTests extends BaseFilteredIntegrationTests {


    private MockHttpServletRequestBuilder helloGetRequestBuilder = get("/api/auth/hello");

    private AccessToken validToken;
    private User testUser;
    private ObjectMapper objectMapper;


    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        TokenController tokenController = new TokenController(new UserRepository(), new TokenRepository( new TokenStringGenerator(1234)));
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        testUser = new User("testUsername", "testPassword");
        session.saveOrUpdate(testUser);
        tx.commit();
        validToken = tokenController.login(testUser);


    }

    @Test
    public void testTokenFilterHello_noToken_Unauthorized() throws Exception {

        mockMvc.perform(helloGetRequestBuilder)
            .andExpect(status().isUnauthorized());

    }

    @Test
    public void testTokenFilterHello_InvalidToken_Unauthorized() throws Exception {

        String invalidTokenValue = new TokenStringGenerator(1235).generateTokenString();
        AccessToken invalidToken = new AccessToken(invalidTokenValue);
        String invalidTokenjson = objectMapper.writeValueAsString(invalidToken);
        mockMvc.perform(helloGetRequestBuilder.header("token", invalidTokenjson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void TokenFilter_validToken_OK() throws Exception {
        String validTokenjson = objectMapper.writeValueAsString(validToken);
        mockMvc.perform(helloGetRequestBuilder.header("token", validTokenjson)).andExpect(status().isOk());
    }

    @After
    public void tearDown() throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        session.delete(testUser);
        tx.commit();
    }
}
