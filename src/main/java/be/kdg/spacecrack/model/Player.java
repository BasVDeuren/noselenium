package be.kdg.spacecrack.model;

import java.util.ArrayList;
import java.util.List;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
public class Player {
    private Contact contact;
    private List<Ship> ships;

    public Player(Contact contact) {
        ships = new ArrayList<Ship>();
        this.contact = contact;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public List<Ship> getShips() {
        return ships;
    }
}
