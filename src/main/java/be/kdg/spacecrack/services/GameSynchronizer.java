package be.kdg.spacecrack.services;

import be.kdg.spacecrack.controllers.GameController;
import be.kdg.spacecrack.model.Game;
import be.kdg.spacecrack.repositories.IGameRepository;
import be.kdg.spacecrack.utilities.IFirebaseUtil;
import be.kdg.spacecrack.utilities.IViewModelConverter;
import be.kdg.spacecrack.viewmodels.GameViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Janne on 12/03/14.
 */
@Component("gameSynchronizer")
public class GameSynchronizer implements IGameSynchronizer {
    @Autowired
    IViewModelConverter viewModelConverter;

    @Autowired
    IFirebaseUtil firebaseUtil;

    @Autowired
    IGameRepository gameRepository;

    GameSynchronizer() {}

    public GameSynchronizer(IViewModelConverter viewModelConverter, IFirebaseUtil firebaseUtil, IGameRepository gameRepository) {
        this.viewModelConverter = viewModelConverter;
        this.firebaseUtil = firebaseUtil;
        this.gameRepository = gameRepository;
    }

    @Override
    public void updateGame(Game game) {

        GameViewModel gameViewModel = viewModelConverter.convertGameToViewModel(game);

        firebaseUtil.setValue(GameController.GAMESUFFIX + gameViewModel.getName(), gameViewModel);
        gameRepository.updateGame(game);
    }
}
