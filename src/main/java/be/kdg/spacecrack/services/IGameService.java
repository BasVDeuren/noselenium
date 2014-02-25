package be.kdg.spacecrack.services;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.*;

import java.util.List;

public interface IGameService {
    int createGame(Profile userProfile, String gameName, Profile opponentProfile);

    void moveShip(Ship ship, String planet);

    Planet getShipLocationByShipId(int shipId);

    void endTurn(int playerId);

    List<Game> getGames(User user);

    Game getGameByGameId(int gameId);

    void checkVictory(Game gameByGameId);

    Player getActivePlayer(User user, Game game);

    Player getOpponentPlayer(User user, Game game);
}
