package be.kdg.spacecrack.commands;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.services.IGameService;

public class EndTurnAction extends Action {

    public EndTurnAction(IGameService gameService, Integer player) {
        super(gameService, player);
    }

    @Override
    public void execute() {
        gameService.endTurn(playerId);
    }


}
