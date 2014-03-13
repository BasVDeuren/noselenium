package be.kdg.spacecrack.viewmodels;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

public class GameActivePlayerWrapper {
    private GameViewModel game;
    private int activePlayerId;
    private String firebaseGameURL;

    public GameActivePlayerWrapper() {}

    public GameActivePlayerWrapper(GameViewModel game, int activePlayerId, String firebaseGameURL) {
        this.game = game;
        this.activePlayerId = activePlayerId;
        this.firebaseGameURL = firebaseGameURL;
    }

    public GameViewModel getGame() {
        return game;
    }

    public void setGame(GameViewModel game) {
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
