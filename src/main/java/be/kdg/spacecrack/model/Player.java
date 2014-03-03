package be.kdg.spacecrack.model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

/**
 * This class represents the player in a specific game, it contains information on his current turn, his resources, and contains his Ships and Colonies on the map.
 */
@Entity
@Table(name = "T_Player")
public class Player {
    @Id
    @GeneratedValue
    private int playerId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profileId")
    private Profile profile;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany
    private List<Colony> colonies;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany
    private List<Ship> ships;

    @Column(name="commandPoints")
    private int commandPoints;

    @Column
    private boolean turnEnded;


    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name= "gameId")
    private Game game;

    public int getCommandPoints() {
        return commandPoints;
    }

    public void setCommandPoints(int commandPoints) {
        this.commandPoints = commandPoints;
    }

    public Player() {
    }

    public Player(Profile profile) {
        this.profile = profile;
    //    this.commandPoints = 5;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setColonies(List<Colony> colonies) {
        this.colonies = colonies;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }


    public List<Colony> getColonies() {
        if(colonies == null)
        {
            colonies = new ArrayList<Colony>();
        }
        return colonies;
    }

    public List<Ship> getShips() {
        if(ships == null)
        {
            ships = new ArrayList<Ship>();
        }
        return ships;
    }

    public boolean isTurnEnded() {
        return turnEnded;
    }

    public void setTurnEnded(boolean turnEnded) {
        this.turnEnded = turnEnded;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
