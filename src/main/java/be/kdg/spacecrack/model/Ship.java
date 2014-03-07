package be.kdg.spacecrack.model;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

//import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "T_Ship")
public class Ship extends Piece {
    @Id
    @GeneratedValue
    private int shipId;

    @ManyToOne
    @JoinColumn(name = "playerId")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "planetId")
    private Planet planet;


    public int getShipId() {
        return shipId;
    }

    public void setShipId(int shipId) {
        this.shipId = shipId;
    }

    public Ship() {
    }

    public Ship(Planet planet) {
        this.planet = planet;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlanet(Planet planet) {
        this.planet = planet;
    }

    public Planet getPlanet() {
        return planet;
    }

    public void setPlayer(Player player) {
        this.player = player;
        player.internalAddShip(this);
    }


    protected void internalSetPlayer(Player player) {
        this.player = player;
    }
}
