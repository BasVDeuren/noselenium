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
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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


}
