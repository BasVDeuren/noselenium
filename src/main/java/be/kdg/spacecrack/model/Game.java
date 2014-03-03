package be.kdg.spacecrack.model;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "T_Game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private int gameId;
    private String name;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private List<Player> players;

    @Column
    private int turnCounter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Game() {
       players = new ArrayList<Player>();
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

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
