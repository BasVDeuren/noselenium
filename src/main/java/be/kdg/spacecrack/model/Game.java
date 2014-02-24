package be.kdg.spacecrack.model;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import javax.persistence.*;

@Entity
@Table(name = "T_Game")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private int gameId;
    private String name;
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name= "player1Id")
    private Player player1;

    @Column
    private int turnCounter;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name= "player2Id")
    private Player player2;

    public Player getPlayer2() {
        return player2;
    }

    public Player getPlayer1() {
        return player1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getTurnCounter() {
        return turnCounter;
    }

    public void setTurnCounter(int turnCounter) {
        this.turnCounter = turnCounter;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }
}
