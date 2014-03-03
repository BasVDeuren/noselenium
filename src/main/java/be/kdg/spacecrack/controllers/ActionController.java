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
import be.kdg.spacecrack.model.Ship;
import be.kdg.spacecrack.repositories.IPlayerRepository;
import be.kdg.spacecrack.repositories.IShipRepository;
import be.kdg.spacecrack.services.IGameService;
import be.kdg.spacecrack.utilities.IFirebaseUtil;
import be.kdg.spacecrack.viewmodels.ActionViewModel;
import be.kdg.spacecrack.viewmodels.GameViewModel;
import be.kdg.spacecrack.viewmodels.IViewModelConverter;
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
    private IGameService gameService;
    @Autowired
    private IPlayerRepository playerRepository;

    @Autowired
    private IShipRepository shipRepository;
    @Autowired
    private IViewModelConverter viewModelConverter;
    @Autowired
    private IFirebaseUtil firebaseUtil;



    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public void executeAction(@RequestBody ActionViewModel actionViewModel) throws IOException {
        if(actionViewModel.getPlayerId() == null || actionViewModel.getPlayerId() < 1)
        {
            throw new SpaceCrackNotAcceptableException("An action must contain a valid playerId");
        }

        if(actionViewModel.getGameId() == null || actionViewModel.getGameId() < 1)
        {
            throw new SpaceCrackNotAcceptableException("An action must contain a valid gameId");
        }




        String actionType = actionViewModel.getActionType();

        if(actionType.equals(MOVESHIP)){
            Ship ship = shipRepository.getShipByShipId(actionViewModel.getShip().getShipId());
            Action moveShipAction = new MoveShipAction(gameService, actionViewModel.getPlayerId(), actionViewModel.getShip(), actionViewModel.getDestinationPlanetName());
            moveShipAction.execute();
        }else if(actionType.equals(ENDTURN)){
            Action endTurnAction = new EndTurnAction(gameService, actionViewModel.getPlayerId());
            endTurnAction.execute();
            //gameService.checkVictory(gameService.getGameByGameId(actionViewModel.getGameId()));
        }else{
            throw new SpaceCrackNotAcceptableException("Unsupported action type");
        }
        Game game = gameService.getGameByGameId(actionViewModel.getGameId());
        GameViewModel gameViewModel = viewModelConverter.convertGameToViewModel(game);

        firebaseUtil.setValue(GameController.GAMESUFFIX +game.getName(), gameViewModel);
    }
}
