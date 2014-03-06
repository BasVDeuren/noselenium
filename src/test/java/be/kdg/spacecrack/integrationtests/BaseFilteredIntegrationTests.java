package be.kdg.spacecrack.integrationtests;

import be.kdg.spacecrack.controllers.*;
import be.kdg.spacecrack.filters.TokenFilter;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.*;
import be.kdg.spacecrack.services.*;
import be.kdg.spacecrack.utilities.FirebaseUtil;
import be.kdg.spacecrack.utilities.ITokenStringGenerator;
import be.kdg.spacecrack.utilities.TokenStringGenerator;
import be.kdg.spacecrack.validators.GameParametersValidator;
import be.kdg.spacecrack.viewmodels.ViewModelConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//import org.codehaus.jackson.map.ObjectMapper;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml", "file:src/test/resources/application-context.xml"})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public abstract class BaseFilteredIntegrationTests {
    protected MockMvc mockMvc;
    @Autowired
    private ServletContext servletContext;
    private WebApplicationContext ctx;
    protected ObjectMapper objectMapper;


    @Autowired
    SessionFactory sessionFactory;

    @Before
    public void setupMockMVC() throws Exception {

        ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);


        IPlanetRepository planetRepository = new PlanetRepository(sessionFactory);
        IColonyRepository colonyRepository = new ColonyRepository(sessionFactory);
        IShipRepository shipRepository = new ShipRepository(sessionFactory);
        IPlayerRepository playerRepository = new PlayerRepository(sessionFactory);
        IGameRepository gameRepository = new GameRepository(sessionFactory);
        IMapFactory mapFactory = new MapFactory(sessionFactory,planetRepository);
        ITokenRepository tokenRepository = new TokenRepository(sessionFactory);
        IProfileRepository profileRepository= new ProfileRepository(sessionFactory);
        IUserRepository userRepository= new UserRepository(sessionFactory);
        ITokenStringGenerator tokenStringGenerator = new TokenStringGenerator();

        IGameService gameService = new GameService(planetRepository,colonyRepository, shipRepository, playerRepository,gameRepository);
        IAuthorizationService authorizationService = new AuthorizationService(tokenRepository, userRepository, tokenStringGenerator);
        IUserService userService = new UserService(userRepository, profileRepository);
        IProfileService profileService = new ProfileService(profileRepository, userRepository);

        ViewModelConverter viewModelConverter = new ViewModelConverter();
        FirebaseUtil firebaseUtil = new FirebaseUtil();
        ActionController actionController = new ActionController(gameService, viewModelConverter, firebaseUtil);
        GameController gameController = new GameController(authorizationService,gameService, profileService, viewModelConverter, firebaseUtil);
        MapController mapController = new MapController(mapFactory);
        ProfileController profileController = new ProfileController(profileService, userService, tokenRepository, authorizationService);
        TokenController tokenController = new TokenController(authorizationService);
        UserController userController = new UserController(userService, authorizationService);
        TokenFilter filter = new TokenFilter(authorizationService);
        FilterConfig filterConfig = new MockFilterConfig(servletContext);

        filter.init(filterConfig);
        GameParametersValidator validator = new GameParametersValidator();

        mockMvc = MockMvcBuilders.standaloneSetup(actionController, gameController, mapController, profileController, tokenController, userController).setValidator(validator).addFilter(filter, "/auth/*").build();
        objectMapper = new ObjectMapper();
    }

    protected String loginAndRetrieveAccessToken() throws Exception {


        User testUser = new User("test", "test", "test@gmail.com");
        String userjson = objectMapper.writeValueAsString(testUser);

        MockHttpServletRequestBuilder requestBuilder = post("/accesstokens");
        String accessTokenJson = mockMvc.perform(requestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .content(userjson)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        AccessToken accessToken = objectMapper.readValue(accessTokenJson, AccessToken.class);
        return "%22" + accessToken.getValue() + "%22";

    }
}
