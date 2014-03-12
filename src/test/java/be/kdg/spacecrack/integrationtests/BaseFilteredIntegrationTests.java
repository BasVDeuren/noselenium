package be.kdg.spacecrack.integrationtests;

import be.kdg.spacecrack.config.AsyncConfig;
import be.kdg.spacecrack.controllers.*;
import be.kdg.spacecrack.filters.TokenHandlerInterceptor;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.Profile;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.repositories.*;
import be.kdg.spacecrack.services.*;
import be.kdg.spacecrack.services.handlers.IMoveShipHandler;
import be.kdg.spacecrack.services.handlers.MoveShipHandler;
import be.kdg.spacecrack.utilities.*;
import be.kdg.spacecrack.validators.BeanValidator;
import be.kdg.spacecrack.viewmodels.GameActivePlayerWrapper;
import be.kdg.spacecrack.viewmodels.GameParameters;
import be.kdg.spacecrack.viewmodels.ProfileWrapper;
import be.kdg.spacecrack.viewmodels.UserViewModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import java.lang.reflect.Method;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    protected static MockMvc mockMvc;
    @Autowired
    protected ServletContext servletContext;
    protected WebApplicationContext ctx;
    protected static ObjectMapper objectMapper;


    @Autowired
    SessionFactory sessionFactory;
    protected static GameController baseGameController;
    protected static StandaloneMockMvcBuilder mvcBuilderWithoutGlobalExceptionHandler;

    @Before
    public void setupMockMVC() throws Exception {

        ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);

        if(mvcBuilderWithoutGlobalExceptionHandler == null) {
            IPlanetRepository planetRepository = new PlanetRepository(sessionFactory);
            IColonyRepository colonyRepository = new ColonyRepository(sessionFactory);
            IShipRepository shipRepository = new ShipRepository(sessionFactory);
            IPlayerRepository playerRepository = new PlayerRepository(sessionFactory);
            IGameRepository gameRepository = new GameRepository(sessionFactory);
            IMapFactory mapFactory = new MapFactory(sessionFactory, planetRepository);
            ITokenRepository tokenRepository = new TokenRepository(sessionFactory);
            IProfileRepository profileRepository = new ProfileRepository(sessionFactory);
            IUserRepository userRepository = new UserRepository(sessionFactory);
            ITokenStringGenerator tokenStringGenerator = new TokenStringGenerator();

            ViewModelConverter viewModelConverter = new ViewModelConverter();
            IFirebaseUtil firebaseUtil = mock(IFirebaseUtil.class);
            IMoveShipHandler moveShipHandler = new MoveShipHandler(colonyRepository, planetRepository, new GameSynchronizer(viewModelConverter, firebaseUtil, gameRepository));

            AsyncConfig asyncConfig = new AsyncConfig();
            IGameService gameService = new GameService(planetRepository, colonyRepository, shipRepository, playerRepository, gameRepository, moveShipHandler, viewModelConverter, mock(GameSynchronizer.class));
            IAuthorizationService authorizationService = new AuthorizationService(tokenRepository, userRepository, tokenStringGenerator);

            IUserService userService = new UserService(userRepository, profileRepository);
            IProfileService profileService = new ProfileService(profileRepository, userRepository);

            ActionController actionController = new ActionController(gameService, viewModelConverter, firebaseUtil);
            baseGameController = new GameController(authorizationService, gameService, profileService, viewModelConverter, firebaseUtil);
            MapController mapController = new MapController(mapFactory);
            ProfileController profileController = new ProfileController(profileService, userService, tokenRepository, authorizationService);
            TokenController tokenController = new TokenController(authorizationService);
            UserController userController = new UserController(userService, authorizationService);
            BeanValidator validator = new BeanValidator();

            ReplayController replayController = new ReplayController(gameService);
            mvcBuilderWithoutGlobalExceptionHandler = MockMvcBuilders.standaloneSetup(actionController, baseGameController, mapController, profileController, tokenController, userController, replayController)
                    .setValidator(validator).addInterceptors(new TokenHandlerInterceptor(authorizationService));
            mockMvc = mvcBuilderWithoutGlobalExceptionHandler.build();
    }

        if(objectMapper == null) {
            objectMapper = new ObjectMapper();
        }

    }

    protected ExceptionHandlerExceptionResolver getGlobalExceptionHandler() {
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {
            protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception exception) {
                Method method = new ExceptionHandlerMethodResolver(GlobalExceptionHandler.class).resolveMethod(exception);
                return new ServletInvocableHandlerMethod(new GlobalExceptionHandler(), method);
            }
        };
        exceptionResolver.afterPropertiesSet();
        return exceptionResolver;
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

    protected Profile createOpponent() throws Exception {
        ProfileWrapper profileWrapper = new ProfileWrapper("pponentname", "opponentlastname", "opponentemail@gmail.com", "12-07-1992", "image");
        String profileWrapperJson = objectMapper.writeValueAsString(profileWrapper);
        String opponentAccessToken = logOpponentIn();

        mockMvc.perform(post("/auth/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(profileWrapperJson)
                .cookie(new Cookie("accessToken", opponentAccessToken)));

        String profileJson = mockMvc.perform(get("/auth/profile")
                .accept(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("accessToken", opponentAccessToken))).andReturn().getResponse().getContentAsString();

        Profile profile = objectMapper.readValue(profileJson, Profile.class);

        return profile;
    }

    private String logOpponentIn() throws Exception {
        UserViewModel opponentUserWrapper = new UserViewModel("opponent", "opponentpw", "opponentpw", "opponent@gmail.com");
        String userWrapperjson = objectMapper.writeValueAsString(opponentUserWrapper);

        String opponentAccessTokenJson = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userWrapperjson)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        AccessToken opponentAccessToken = objectMapper.readValue(opponentAccessTokenJson, AccessToken.class);

        String userjson = mockMvc.perform(get("/auth/user")
                .cookie(new Cookie("accessToken", "%22" + opponentAccessToken.getValue() + "%22"))).andReturn().getResponse().getContentAsString();

        MockHttpServletRequestBuilder requestBuilder = post("/accesstokens");
        String accessTokenJson = mockMvc.perform(requestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .content(userjson)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        AccessToken accessToken = objectMapper.readValue(accessTokenJson, AccessToken.class);
        return "%22" + accessToken.getValue() + "%22";
    }

    protected GameActivePlayerWrapper createAGame(String accessToken) throws Exception {
        Profile opponentProfile = createOpponent();

        GameParameters gameParameters = new GameParameters("SpacecrackGame1", opponentProfile.getProfileId());
        String gameParametersJson = objectMapper.writeValueAsString(gameParameters);

        String gameIdJson = mockMvc.perform(post("/auth/game")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gameParametersJson)
                .cookie(new Cookie("accessToken", accessToken))).andReturn().getResponse().getContentAsString();
        System.out.println(gameIdJson);
        int gameId = objectMapper.readValue(gameIdJson, Integer.class);

        String gameJson = mockMvc.perform(get("/auth/game/specificGame/" + gameId)
                .accept(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("accessToken", accessToken))).andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(gameJson, GameActivePlayerWrapper.class);
    }
}
