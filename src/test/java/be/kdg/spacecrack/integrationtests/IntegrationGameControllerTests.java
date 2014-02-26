package be.kdg.spacecrack.integrationtests;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.viewmodels.*;
import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.Game;
import be.kdg.spacecrack.model.Profile;
import be.kdg.spacecrack.model.Ship;
import be.kdg.spacecrack.utilities.HibernateUtil;
import org.hamcrest.CoreMatchers;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IntegrationGameControllerTests extends BaseFilteredIntegrationTests {
    Profile opponentProfile;
    @Test
    public void testCreateGame_AuthorisedUser_Map() throws Exception {
        String accessToken = login();

        Profile opponentProfile = createOpponent();

        GameParameters gameParameters = new GameParameters("SpacecrackGame1", ""+opponentProfile.getProfileId());
        String gameParametersJson = objectMapper.writeValueAsString(gameParameters);

        String gameIdJson = mockMvc.perform(post("/auth/game")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gameParametersJson)
                .cookie(new Cookie("accessToken", accessToken))).andReturn().getResponse().getContentAsString();
        int gameId = objectMapper.readValue(gameIdJson, Integer.class);

        mockMvc.perform(get("/auth/game/specificGame/" + gameId)
                .accept(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("accessToken", accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.game.player1.colonies[0].planetName", CoreMatchers.is("a")))
                .andExpect(jsonPath("$.game.player1.ships[0].planetName", CoreMatchers.is("a")))
                .andExpect(jsonPath("$.game.player1.ships[0].shipId", CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.game.player1.ships[0].shipId", CoreMatchers.not(0)))
                .andExpect(jsonPath("$.game.player2.colonies[0].planetName", CoreMatchers.is("a3")))
                .andExpect(jsonPath("$.game.player2.ships[0].planetName", CoreMatchers.is("a3")))
                .andExpect(jsonPath("$.game.player2.ships[0].shipId", CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.game.player2.ships[0].shipId", CoreMatchers.not(0)));
    }

    @Test
    public void MoveShip_validPlanet_Ok() throws Exception {
        String accessToken = login();
        Profile opponentProfile = createOpponent();

        GameParameters gameParameters = new GameParameters("SpacecrackGame1", ""+opponentProfile.getProfileId());
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

        GameActivePlayerWrapper gameActivePlayerWrapper = objectMapper.readValue(gameJson, GameActivePlayerWrapper.class);
        Game game = gameActivePlayerWrapper.getGame();

        Ship ship = game.getPlayer1().getShips().get(0);
        String destinationPlanet = "b";

        ActionViewModel moveShipActionViewModel = new ActionViewModel("MOVESHIP", ship, destinationPlanet, null, game.getGameId());

        String moveShipActionJson = objectMapper.writeValueAsString(moveShipActionViewModel);

        mockMvc.perform(post("/auth/action")
                .contentType(MediaType.APPLICATION_JSON)
                .content(moveShipActionJson)
                .cookie(new Cookie("accessToken", accessToken)))
                .andExpect(status().isOk());
    }

    private Profile createOpponent() throws Exception {
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
        UserWrapper opponentUserWrapper = new UserWrapper("opponent", "opponentpw", "opponentpw", "opponent@gmail.com");
        String userWrapperjson = objectMapper.writeValueAsString(opponentUserWrapper);

        String opponentAccessTokenJson = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userWrapperjson)).andReturn().getResponse().getContentAsString();

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


    @Test
    public void getAllGamesByProfile() throws Exception {
        String accessToken = login();

        Profile opponentProfile = createOpponent();

        GameParameters gameParameters = new GameParameters("SpacecrackGame1", ""+ opponentProfile.getProfileId());
        String gameParametersJson = objectMapper.writeValueAsString(gameParameters);

        mockMvc.perform(post("/auth/game")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gameParametersJson)
                .cookie(new Cookie("accessToken", accessToken)));


        mockMvc.perform(get("/auth/game")
                .accept(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("accessToken", accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].gameId", CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.[0].name", CoreMatchers.notNullValue()));
    }

    @Test
    public void getGameByGameId() throws Exception {
        String accessToken = login();
        Profile opponentProfile = createOpponent();

        GameParameters gameParameters = new GameParameters("SpacecrackGame1", ""+ opponentProfile.getProfileId());
        String gameParametersJson = objectMapper.writeValueAsString(gameParameters);

        String gameIdJson = mockMvc.perform(post("/auth/game")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gameParametersJson)
                .cookie(new Cookie("accessToken", accessToken))).andReturn().getResponse().getContentAsString();
        System.out.println(gameIdJson);
        int gameId = objectMapper.readValue(gameIdJson, Integer.class);

        String gameActivePlayerJson = mockMvc.perform(get("/auth/game/specificGame/" + gameId)
                .accept(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("accessToken", accessToken))).andReturn().getResponse().getContentAsString();

        GameActivePlayerWrapper gameActivePlayerWrapper = objectMapper.readValue(gameActivePlayerJson, GameActivePlayerWrapper.class);
        Game expected = gameActivePlayerWrapper.getGame();


        mockMvc.perform(get("/auth/game/specificGame/" + expected.getGameId())
                .accept(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("accessToken", accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.game.gameId", CoreMatchers.notNullValue()));
    }

    @Test
    public void endPlayerTurn() throws Exception {
        String accessToken = login();
        Profile opponentProfile = createOpponent();

        GameParameters gameParameters = new GameParameters("SpacecrackGame1", ""+ opponentProfile.getProfileId());
        String gameParametersJson = objectMapper.writeValueAsString(gameParameters);

        String gameIdJson = mockMvc.perform(post("/auth/game")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gameParametersJson)
                .cookie(new Cookie("accessToken", accessToken))).andReturn().getResponse().getContentAsString();
        System.out.println(gameIdJson);
        int gameId = objectMapper.readValue(gameIdJson, Integer.class);

        String gameActivePlayerJson = mockMvc.perform(get("/auth/game/specificGame/" + gameId)
                .accept(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("accessToken", accessToken))).andReturn().getResponse().getContentAsString();

        GameActivePlayerWrapper gameActivePlayerWrapper = objectMapper.readValue(gameActivePlayerJson, GameActivePlayerWrapper.class);
Game game = gameActivePlayerWrapper.getGame();

        mockMvc.perform(post("/auth/action")

                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ActionViewModel("ENDTURN", game.getPlayer1().getShips().get(0), "", game.getPlayer1().getPlayerId(), 0)))
                .cookie(new Cookie("accessToken", accessToken))
        ).andExpect(status().isOk());
    }

    @After
    public void tearDown() throws Exception {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            Transaction tx = session.beginTransaction();
            try {
                @SuppressWarnings("JpaQlInspection")Query q = session.createQuery("delete from User");
                q.executeUpdate();
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
            }
        } finally {
            HibernateUtil.close(session);
        }
    }
}
