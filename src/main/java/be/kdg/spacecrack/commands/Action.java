package be.kdg.spacecrack.commands;
/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Player;
import be.kdg.spacecrack.services.IGameService;


public abstract class Action {


    protected IGameService gameService;

    protected Player player;



    protected Action(IGameService gameService, Player player) {
        this.gameService = gameService;

        this.player = player;
    }

    public abstract void execute();


}

