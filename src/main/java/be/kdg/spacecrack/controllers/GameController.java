package be.kdg.spacecrack.controllers;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Game;
import be.kdg.spacecrack.model.User;
import be.kdg.spacecrack.services.IAuthorizationService;
import be.kdg.spacecrack.services.IGameService;
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

    public GameController() {
    }

    public GameController(IAuthorizationService authorizationService, IGameService gameService ){
        this.authorizationService = authorizationService;
        this.gameService = gameService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Game createGame(@CookieValue("accessToken") String accessTokenValue){
        User user = authorizationService.getUserByAccessTokenValue(accessTokenValue);
        Game game = gameService.createGame(user.getProfile());
        return game;

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
