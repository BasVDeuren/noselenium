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

    private PlayerViewModel player1;


    private int turnCounter;

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

    public int getTurnCounter() {
        return turnCounter;
    }

    public void setTurnCounter(int turnCounter) {
        this.turnCounter = turnCounter;
    }

    public PlayerViewModel getPlayer2() {
        return player2;
    }

    public void setPlayer2(PlayerViewModel player2) {
        this.player2 = player2;
    }
}
