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
    String activePlayerFirebaseURL;
    String opponentFirebaseURL;

    public GameActivePlayerWrapper() {
    }

    public GameActivePlayerWrapper(Game game, int activePlayerId, String activePlayerFirebaseURL, String opponentFirebaseURL) {
        this.game = game;
        this.activePlayerId = activePlayerId;
        this.activePlayerFirebaseURL = activePlayerFirebaseURL;
        this.opponentFirebaseURL = opponentFirebaseURL;
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

    public String getActivePlayerFirebaseURL() {
        return activePlayerFirebaseURL;
    }

    public void setActivePlayerFirebaseURL(String activePlayerFirebaseURL) {
        this.activePlayerFirebaseURL = activePlayerFirebaseURL;
    }

    public String getOpponentFirebaseURL() {
        return opponentFirebaseURL;
    }

    public void setOpponentFirebaseURL(String opponentFirebaseURL) {
        this.opponentFirebaseURL = opponentFirebaseURL;
    }
}
