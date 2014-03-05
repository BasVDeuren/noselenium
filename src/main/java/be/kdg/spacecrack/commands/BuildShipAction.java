package be.kdg.spacecrack.commands;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.services.IGameService;
import be.kdg.spacecrack.viewmodels.ColonyViewModel;

public class BuildShipAction extends Action {
    private final ColonyViewModel colony;

    public BuildShipAction(IGameService gameService, Integer playerId, ColonyViewModel colony) {
        super(gameService, playerId);
        this.colony = colony;
    }

    @Override
    public void execute() {
        gameService.buildShip(colony.getColonyId());
    }
}
