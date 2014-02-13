package be.kdg.spacecrack.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tim on 13/02/14.
 */
public class Game {

    private List<Player> players;
    private SpaceCrackMap spaceCrackMap;

    public Game(Player creatingPlayer, Player oppenentPlayer, SpaceCrackMap spaceCrackMap) {
        this.spaceCrackMap = spaceCrackMap;
        players = new ArrayList<Player>();
        players.add(creatingPlayer);
        players.add(oppenentPlayer);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public SpaceCrackMap getSpaceCrackMap() {
        return spaceCrackMap;
    }
}
