package be.kdg.spacecrack.viewmodels;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

public class GameParameters {
    String gameName;
    int opponentProfileId;

    public GameParameters() {
    }

    public GameParameters(String gameName, int opponentProfileId) {
        this.gameName = gameName;
        this.opponentProfileId = opponentProfileId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getOpponentProfileId() {
        return opponentProfileId;
    }

    public void setOpponentProfileId(int opponentProfileId) {
        this.opponentProfileId = opponentProfileId;
    }
}
