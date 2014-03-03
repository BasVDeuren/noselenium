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
    public static final int START_COMMAND_POINTS = 5;
    public static final int COMMANDPOINTSPERTURN = START_COMMAND_POINTS;
    public static final int BUILDSHIPCOST = 3;
    public static final int MOVESHIPCOST = 1;
    public static final int CREATECOLONYCOST = 1;
    public static final int NEWSHIPSTRENGTH = 1;

    int createGame(Profile userProfile, String gameName, Profile opponentProfile);

    void moveShip(Integer shipId, String planetName);

    Planet getShipLocationByShipId(int shipId);


    void endTurn(Integer playerID);

    List<Game> getGames(User user);

    Game getGameByGameId(int gameId);

    void checkVictory(Game gameByGameId);

    Player getActivePlayer(User user, Game game);

    void buildShip(Integer colonyId);
}
