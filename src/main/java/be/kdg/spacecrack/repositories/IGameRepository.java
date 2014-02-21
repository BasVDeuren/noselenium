package be.kdg.spacecrack.repositories;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Game;
import be.kdg.spacecrack.model.Profile;

import java.util.List;

public interface IGameRepository {

    int createGame(Game game);

    List<Game> getGamesByProfile(Profile profile);

    Game getGameByGameId(int gameId);
}
