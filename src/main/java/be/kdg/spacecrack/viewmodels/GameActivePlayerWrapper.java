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
    String firebaseGameURL;

    public GameActivePlayerWrapper() {
    }

    public GameActivePlayerWrapper(Game game, int activePlayerId, String firebaseGameURL) {
        this.game = game;
        this.activePlayerId = activePlayerId;
        this.firebaseGameURL = firebaseGameURL;
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

    public String getFirebaseGameURL() {
        return firebaseGameURL;
    }

    public void setFirebaseGameURL(String firebaseGameURL) {
        this.firebaseGameURL = firebaseGameURL;
    }
}
