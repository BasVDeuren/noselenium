package be.kdg.spacecrack.viewmodels;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Colony;
import be.kdg.spacecrack.model.Game;
import be.kdg.spacecrack.model.Player;
import be.kdg.spacecrack.model.Ship;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("viewModelConverter")
public class ViewModelConverter implements IViewModelConverter {
    @Override
    public ColonyViewModel convertColonyToViewModel(Colony colony) {
        ColonyViewModel colonyViewModel = new ColonyViewModel();
        colonyViewModel.setColonyId(colony.getColonyId());
        colonyViewModel.setPlanetName(colony.getPlanet().getName());
        colonyViewModel.setStrength(colony.getStrength());
        return colonyViewModel;
    }

    @Override
    public GameViewModel convertGameToViewModel(Game game)
    {
        GameViewModel gameViewModel = new GameViewModel();
        gameViewModel.setGameId(game.getGameId());
        gameViewModel.setName(game.getName());
        gameViewModel.setLoserPlayerId(game.getLoserPlayerId());

        PlayerViewModel player1ViewModel = convertPlayerToViewModel(game.getPlayers().get(0));
        gameViewModel.setPlayer1(player1ViewModel);
        PlayerViewModel player2ViewModel = convertPlayerToViewModel(game.getPlayers().get(1));
        gameViewModel.setPlayer2(player2ViewModel);
        return gameViewModel;
    }

    @Override
    public PlayerViewModel convertPlayerToViewModel(Player player)
   {
       PlayerViewModel playerViewModel = new PlayerViewModel();
       playerViewModel.setCommandPoints(player.getCommandPoints());
       playerViewModel.setTurnEnded(player.isTurnEnded());
       playerViewModel.setPlayerId(player.getPlayerId());
       List<ShipViewModel> shipViewModels  = new ArrayList<ShipViewModel>();
       for(Ship ship: player.getShips())
       {
           ShipViewModel shipViewModel = convertShipToViewModel(ship);

           shipViewModels.add(shipViewModel);
       }
       playerViewModel.setShips(shipViewModels);
       List<ColonyViewModel> colonyViewModels = new ArrayList<ColonyViewModel>();
       for(Colony colony: player.getColonies())
       {
           ColonyViewModel colonyViewModel = convertColonyToViewModel(colony);
           colonyViewModels.add(colonyViewModel);

       }
       playerViewModel.setColonies(colonyViewModels);
       return playerViewModel;
   }

    @Override
    public ShipViewModel convertShipToViewModel(Ship ship) {
        ShipViewModel shipViewModel = new ShipViewModel();
        shipViewModel.setShipId(ship.getShipId());
        shipViewModel.setPlanetName(ship.getPlanet().getName());
        shipViewModel.setStrength(ship.getStrength());
        return shipViewModel;
    }
}
