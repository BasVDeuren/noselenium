package be.kdg.spacecrack.services;

import be.kdg.spacecrack.model.Contact;
import be.kdg.spacecrack.model.Game;

/**
 * Created by Tim on 13/02/14.
 */
public class GameService {
    public Game createGame(Contact creator, Contact opponent) {
        return new Game(creator, opponent);
    }
}
