package be.kdg.spacecrack.viewmodels;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

public class GameViewModel {

    private int gameId;
    private String name;
    private int loserPlayerId;

    private PlayerViewModel player1;

    private PlayerViewModel player2;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayerViewModel getPlayer1() {
        return player1;
    }

    public void setPlayer1(PlayerViewModel player1) {
        this.player1 = player1;
    }

    public PlayerViewModel getPlayer2() {
        return player2;
    }

    public void setPlayer2(PlayerViewModel player2) {
        this.player2 = player2;
    }

    public int getLoserPlayerId() {
        return loserPlayerId;
    }

    public void setLoserPlayerId(int loserPlayerId) {
        this.loserPlayerId = loserPlayerId;
    }
}
