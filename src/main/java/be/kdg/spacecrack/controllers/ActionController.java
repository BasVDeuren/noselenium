package be.kdg.spacecrack.controllers;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.Exceptions.SpaceCrackNotAcceptableException;
import be.kdg.spacecrack.viewmodels.ActionViewModel;
import be.kdg.spacecrack.services.IGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/auth/action")
public class ActionController {
    public static final String MOVESHIP = "MOVESHIP";
    public static final String ENDTURN = "ENDTURN";

    @Autowired
    IGameService gameService;

    public ActionController() {
    }

    public ActionController(IGameService gameService){
        this.gameService = gameService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public void executeAction(@RequestBody ActionViewModel actionViewModel){
        if(actionViewModel.getActionType().equals(MOVESHIP)){
            gameService.moveShip(actionViewModel.getShip(), actionViewModel.getDestinationPlanetName());
        }else if(actionViewModel.getActionType().equals(ENDTURN)){
            gameService.endTurn(actionViewModel.getPlayerId());
            //gameService.checkVictory(gameService.getGameByGameId(actionViewModel.getGameId()));
        }else{
            throw new SpaceCrackNotAcceptableException("Unsupported action type");
        }
    }
}
