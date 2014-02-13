package be.kdg.spacecrack.services;

import be.kdg.spacecrack.controllers.MapController;
import be.kdg.spacecrack.model.*;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
public class GameService {
    public Game createGame(Contact creator, Contact opponent) {
        Player creatingPlayer = new Player(creator);
        Player oppenentPlayer = new Player(opponent);
        //TODO: CHANGE THIS ASAP THIS IS RLY RLY BAD
        MapController controller = new MapController();
        SpaceCrackMap map = controller.getMap();
map.getPlanets()[0].setPlayer(creatingPlayer);
        return new Game(creatingPlayer, oppenentPlayer, map);

    }
}
