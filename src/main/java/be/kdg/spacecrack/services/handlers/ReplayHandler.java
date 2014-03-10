package be.kdg.spacecrack.services.handlers;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.model.Game;
import be.kdg.spacecrack.utilities.IFirebaseUtil;
import be.kdg.spacecrack.utilities.IViewModelConverter;
import be.kdg.spacecrack.viewmodels.GameViewModel;

import java.util.ArrayList;
import java.util.List;


public class ReplayHandler implements Runnable {

    IViewModelConverter viewModelConverter;

    IFirebaseUtil firebaseUtil;

    List<Game> gameRevisions = new ArrayList<Game>();
    private String firebaseURL;

    public ReplayHandler(IViewModelConverter viewModelConverter, IFirebaseUtil firebaseUtil, List<Game> gameRevisions, String firebaseURL) {
        this.viewModelConverter = viewModelConverter;
        this.firebaseUtil = firebaseUtil;
        this.gameRevisions = gameRevisions;
        this.firebaseURL = firebaseURL;
    }

    @Override
    public void run() {
        System.out.println("started Replay");
        for (Game gameRevision : gameRevisions) {
            GameViewModel gameViewModel = viewModelConverter.convertGameToViewModel(gameRevision);
            firebaseUtil.setValue(firebaseURL, gameViewModel);
        }
    }
}
