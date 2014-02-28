package be.kdg.spacecrack.controllers;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.Exceptions.SpaceCrackNotAcceptableException;
import be.kdg.spacecrack.commands.Action;
import be.kdg.spacecrack.commands.EndTurnAction;
import be.kdg.spacecrack.commands.MoveShipAction;
import be.kdg.spacecrack.model.Game;
import be.kdg.spacecrack.model.Player;
import be.kdg.spacecrack.repositories.IPlayerRepository;
import be.kdg.spacecrack.services.IGameService;
import be.kdg.spacecrack.viewmodels.ActionViewModel;
import com.firebase.client.Firebase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;


@Controller
@RequestMapping("/auth/action")
public class ActionController {
    public static final String MOVESHIP = "MOVESHIP";
    public static final String ENDTURN = "ENDTURN";

    @Autowired
    IGameService gameService;
    @Autowired
    IPlayerRepository playerRepository;


    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public void executeAction(@RequestBody ActionViewModel actionViewModel) throws IOException {
        Player player = playerRepository.getPlayerByPlayerId(actionViewModel.getPlayerId());
        if(actionViewModel.getActionType().equals(MOVESHIP)){
     //       gameService.moveShip(actionViewModel.getShip(), actionViewModel.getDestinationPlanetName());

            Action moveShipAction = new MoveShipAction(gameService, player, actionViewModel.getShip(), actionViewModel.getDestinationPlanetName());
            moveShipAction.execute();
        }else if(actionViewModel.getActionType().equals(ENDTURN)){
            Action endTurnAction = new EndTurnAction(gameService, player);
            endTurnAction.execute();
            //gameService.checkVictory(gameService.getGameByGameId(actionViewModel.getGameId()));
        }else{
            throw new SpaceCrackNotAcceptableException("Unsupported action type");
        }
        Game game = gameService.getGameByGameId(actionViewModel.getGameId());
        Firebase ref = new Firebase(GameController.FIREBASEURLBASE + game.getName());

        ref.setValue(game);
    }
}
