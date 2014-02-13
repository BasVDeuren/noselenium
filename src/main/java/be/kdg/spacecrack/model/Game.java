package be.kdg.spacecrack.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Tim on 13/02/14.
 */
public class Game {
    private Set<Contact> players;
    private Contact gameOwner;

    public Game(Contact gameOwner, Contact... opponents) {
        this.gameOwner = gameOwner;

        this.players = new HashSet<Contact>(Arrays.asList(opponents));
        players.add(gameOwner);
    }

    public Set<Contact> getPlayers() {
        return players;
    }
}
