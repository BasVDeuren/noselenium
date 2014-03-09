package be.kdg.spacecrack.commands;
/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.services.IGameService;

/**
 * Command pattern, the gameservice isn't injected by spring here because
 * using @Configurable isn't possible on a normal Tomcat Server, because it doesn't support Load-time Weaving.
 */
public abstract class Action {


    protected IGameService gameService;

    protected Integer playerId;



    protected Action(IGameService gameService, Integer playerId) {
        this.gameService = gameService;

        this.playerId = playerId;
    }

    public abstract void execute();

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }
}

