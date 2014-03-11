package be.kdg.spacecrack.integrationtests;

import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.UserRepository;
import be.kdg.spacecrack.viewmodels.UserViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//import org.codehaus.jackson.map.ObjectMapper;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
public class IntegrationUserTests extends BaseFilteredIntegrationTests {

    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        userRepository = new UserRepository(sessionFactory);
    }

    @Test
    public void testEditUser_validEditedUser_StatusOk() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        Session session = sessionFactory.getCurrentSession();

        User testUser = new User("usernameTest²", "password", "email");
        session.saveOrUpdate(testUser);


        String userjson = objectMapper.writeValueAsString(testUser);

        MockHttpServletRequestBuilder requestBuilder = post("/accesstokens");
        mockMvc.perform(requestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .content(userjson)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();


        String userMapperJsonValid = objectMapper.writeValueAsString(new UserViewModel("usernameTest", "password", "password", "newEmail"));

        MockHttpServletRequestBuilder putRequestBuilder = post("/auth/user");
        String tokenOfEditedUser = userRepository.getUserByUsername(testUser.getUsername()).getToken().getValue();
        mockMvc.perform(putRequestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .content(userMapperJsonValid)
                .cookie(new Cookie("accessToken", "%22" + tokenOfEditedUser + "%22"))
        ).andExpect(status().isOk());
    }


    @Test
    public void testRegisterUser_NewUser_Token() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String validUserWrapperJson = objectMapper.writeValueAsString(new UserViewModel("usernameTest", "password", "password", "email@gmail.com"));
        MockHttpServletRequestBuilder postRequestBuilder = post("/user");
        mockMvc.perform(postRequestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(validUserWrapperJson))
                .andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.value", CoreMatchers.notNullValue()));
    }

    @Test
    public void testRegisterUser_SameUserTwice_Conflict() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String validUserWrapperJson = objectMapper.writeValueAsString(new UserViewModel("usernameTest", "password", "password", "email@gmail.com"));
        MockHttpServletRequestBuilder postRequestBuilder = post("/user");
        mockMvc.perform(postRequestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(validUserWrapperJson));


        mockMvc.perform(postRequestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(validUserWrapperJson))
                .andExpect(status().isConflict());
    }

    @Test
    public void testRegisterUser_Badrepeat_NotAcceptable() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String invalidUserWrapper = objectMapper.writeValueAsString(new UserViewModel("username", "password", "badpassword", "email@gmail.com"));
        MockHttpServletRequestBuilder postRequestBuilder = post("/user");
        MockMvc mockMvcWithoutGlobalExceptionHandler = mvcBuilderWithoutGlobalExceptionHandler.build();

        mockMvcWithoutGlobalExceptionHandler.perform(postRequestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(invalidUserWrapper))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void testGetUser_validToken_User() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        String UserWrapper = objectMapper.writeValueAsString(new UserViewModel("username", "password", "password", "tim.schmitte@gmail.com"));
        MockHttpServletRequestBuilder postRequestBuilder = post("/user");
        mockMvc.perform(postRequestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(UserWrapper));


        User user = userRepository.getUserByUsername("username");

        MockHttpServletRequestBuilder getUserRequestBuilder = get("/auth/user")
                .cookie(new Cookie("accessToken", "%22" + user.getToken().getValue() + "%22"));

        mockMvc.perform(getUserRequestBuilder).andExpect(status().isOk());
    }

    @Test
    public void testGetUser_InvalidToken_SpaceCrackUnauthorisedException() throws Exception {
        //   testRegisterUser_NewUser_Token();

        MockHttpServletRequestBuilder getUserRequestBuilder = get("/auth/user")
                .cookie(new Cookie("accessToken", "invalidToken"));

        mockMvc.perform(getUserRequestBuilder).andExpect(status().isUnauthorized());
    }

}
