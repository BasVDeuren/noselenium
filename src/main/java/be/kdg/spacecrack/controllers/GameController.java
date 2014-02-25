package be.kdg.spacecrack.controllers;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.Exceptions.SpaceCrackNotAcceptableException;
import be.kdg.spacecrack.model.Game;
import be.kdg.spacecrack.model.Player;
import be.kdg.spacecrack.model.Profile;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.services.IAuthorizationService;
import be.kdg.spacecrack.services.IGameService;
import be.kdg.spacecrack.services.IProfileService;
import be.kdg.spacecrack.viewmodels.GameActivePlayerWrapper;
import be.kdg.spacecrack.viewmodels.GameParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller(value = "/auth/game")
public class GameController {
    public static final String FIREBASEURLBASE = "https://vivid-fire-9476.firebaseio.com/player";

    @Autowired
    private IAuthorizationService authorizationService;

    @Autowired
    private IGameService gameService;

    @Autowired
    IProfileService profileService;

    public GameController() {
    }

    public GameController(IAuthorizationService authorizationService, IGameService gameService, IProfileService profileService){
        this.authorizationService = authorizationService;
        this.gameService = gameService;
        this.profileService = profileService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Integer createGame(@CookieValue("accessToken") String accessTokenValue, @RequestBody GameParameters gameData){
        User user = authorizationService.getUserByAccessTokenValue(accessTokenValue);

        int profileId = 0;
        try {
            profileId = Integer.parseInt(gameData.getOpponentProfileId());
        } catch (NumberFormatException e) {
            throw new SpaceCrackNotAcceptableException("Unexpected numberformat");
        }
        Profile opponentProfile =  profileService.getProfileByProfileId(profileId);
        int gameId = gameService.createGame(user.getProfile(), gameData.getGameName(), opponentProfile);
        return gameId;

    }

    @RequestMapping(value = "/auth/game/specificGame/{gameId}", method = RequestMethod.GET)
    @ResponseBody
    public GameActivePlayerWrapper getGameByGameId(@CookieValue("accessToken") String accessTokenValue, @PathVariable String gameId) {
        Game game = gameService.getGameByGameId(Integer.parseInt(gameId));
        User user = authorizationService.getUserByAccessTokenValue(accessTokenValue);
        Player player = gameService.getActivePlayer(user, game);
        Player opponent = gameService.getOpponentPlayer(user, game);

        GameActivePlayerWrapper gameActivePlayerWrapper = new GameActivePlayerWrapper(game, player.getPlayerId(), FIREBASEURLBASE + player.getPlayerId(), FIREBASEURLBASE + opponent.getPlayerId());

        return gameActivePlayerWrapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Game> getGamesByAccessToken(@CookieValue("accessToken") String accessTokenValue) {

        User user = authorizationService.getUserByAccessTokenValue(accessTokenValue);
        List<Game> games = gameService.getGames(user);


        return games;

    }



}
