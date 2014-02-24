package be.kdg.spacecrack.viewmodels;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Game;

public class GameActivePlayerWrapper {
    Game game;
    int activePlayerId;

    public GameActivePlayerWrapper() {
    }

    public GameActivePlayerWrapper(Game game, int activePlayerId) {
        this.game = game;
        this.activePlayerId = activePlayerId;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getActivePlayerId() {
        return activePlayerId;
    }

    public void setActivePlayerId(int activePlayerId) {
        this.activePlayerId = activePlayerId;
    }
}
