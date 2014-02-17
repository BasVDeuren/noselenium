package be.kdg.spacecrack.controllers;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.jsonviewmodels.Action;
import be.kdg.spacecrack.services.IGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/auth/action")
public class ActionController {
    public static final String MOVESHIP = "MOVESHIP";

    @Autowired
    IGameService gameService;

    public ActionController() {
    }

    public ActionController(IGameService gameService){
        this.gameService = gameService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void executeAction(@RequestBody Action action){
        if(action.getActionType().equals(MOVESHIP)){
            gameService.moveShip(action.getShip(), action.getDestinationPlanet());
        }
    }
}
