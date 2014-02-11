package be.kdg.spacecrack.integrationtests;

import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.modelwrapper.UserWrapper;
import be.kdg.spacecrack.repositories.UserRepository;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.codehaus.jackson.map.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Ikke on 10-2-14.
 */
public class IntegrationUserTests extends BaseFilteredIntegrationTests {

    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        userRepository = new UserRepository();
    }

    @Test
    public void testEditUser_validEditedUser_StatusOk() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        User testUser = new User("username", "password", "email");
        session.saveOrUpdate(testUser);
        tx.commit();

        String userjson = objectMapper.writeValueAsString(testUser);

        MockHttpServletRequestBuilder requestBuilder = post("/accesstokens");
        mockMvc.perform(requestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .content(userjson)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();


        String userMapperJsonValid = objectMapper.writeValueAsString(new UserWrapper("username", "password", "password", "newEmail"));
        String accessTokenJsonValid = objectMapper.writeValueAsString(new UserRepository().getUserByUsername(testUser.getUsername()).getToken());

        MockHttpServletRequestBuilder putRequestBuilder = put("/user").contentType(MediaType.APPLICATION_JSON).content(userMapperJsonValid).header("token", accessTokenJsonValid);
        mockMvc.perform(putRequestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .content(userMapperJsonValid)
                .header("token", accessTokenJsonValid)
        ).andExpect(status().isOk());
    }


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

    @Test
    public void testGetUser_validToken_User() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        testRegisterUser_NewUser_Token();
        User user = userRepository.getUserByUsername("username");
        AccessToken accessToken = user.getToken();

        String validToken = objectMapper.writeValueAsString(accessToken);
        MockHttpServletRequestBuilder getUserRequestBuilder = get("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("token", validToken);

        mockMvc.perform(getUserRequestBuilder).andExpect(status().isOk());
    }

    @Test
    public void testGetUser_InvalidToken_SpaceCrackUnauthorisedException() throws Exception {
        testRegisterUser_NewUser_Token();

        MockHttpServletRequestBuilder getUserRequestBuilder = get("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("token", "invalidToken");

        mockMvc.perform(getUserRequestBuilder).andExpect(status().isUnauthorized());
    }

    /*
    @Test
    public void testGetUser_TokenDeletedFromDb_SpaceCrackUnauthorisedException() throws Exception {
        testRegisterUser_NewUser_Token();

        AccessToken accessToken = userRepository.getUserByUsername("username").login();

        userRepository.DeleteAccessToken(accessToken);

        MockHttpServletRequestBuilder getUserRequestBuilder = get("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("token", accessToken);

        mockMvc.perform(getUserRequestBuilder).andExpect(status().isUnauthorized());
    }*/

    @After
    public void tearDown() throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Transaction tx = session.beginTransaction();
            try {
                @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("delete from User");
                q.executeUpdate();
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        } finally {
            HibernateUtil.close(session);
        }
    }
}
