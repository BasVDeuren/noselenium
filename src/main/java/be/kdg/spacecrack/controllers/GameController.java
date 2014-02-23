package be.kdg.spacecrack.controllers;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.jsonviewmodels.GameParameters;
import be.kdg.spacecrack.model.Game;
import be.kdg.spacecrack.model.Profile;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.services.IAuthorizationService;
import be.kdg.spacecrack.services.IGameService;
import be.kdg.spacecrack.services.IProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller(value = "/auth/game")
public class GameController {

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
    public int createGame(@CookieValue("accessToken") String accessTokenValue, @RequestBody GameParameters gameData){
        User user = authorizationService.getUserByAccessTokenValue(accessTokenValue);
        Profile opponentProfile =  profileService.getProfileByProfileId(gameData.getOpponentProfileId());
        Game game = gameService.createGame(user.getProfile(), gameData.getGameName(), opponentProfile);
        return game.getGameId();

    }

    @RequestMapping(value = "/auth/game/specificGame/{gameId}", method = RequestMethod.GET)
    @ResponseBody
    public Game getGameByGameId(@CookieValue("accessToken") String accessTokenValue, @PathVariable String gameId) {
        Game game = gameService.getGameByGameId(Integer.parseInt(gameId));
        return game;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Game> getGamesByAccessToken(@CookieValue("accessToken") String accessTokenValue) {

        User user = authorizationService.getUserByAccessTokenValue(accessTokenValue);
        List<Game> games = gameService.getGames(user);


        return games;

    }



}
