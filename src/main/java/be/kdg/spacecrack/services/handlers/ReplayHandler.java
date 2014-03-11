package be.kdg.spacecrack.services.handlers;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.utilities.IFirebaseUtil;
import be.kdg.spacecrack.viewmodels.GameViewModel;

import java.util.ArrayList;
import java.util.List;


public class ReplayHandler extends Thread {


    IFirebaseUtil firebaseUtil;

    List<GameViewModel> gameRevisions = new ArrayList<GameViewModel>();
    private String firebaseURL;

    public ReplayHandler(IFirebaseUtil firebaseUtil, List<GameViewModel> gameRevisionViewModels, String firebaseURL) {
        this.firebaseUtil = firebaseUtil;
        this.gameRevisions = gameRevisionViewModels;
        this.firebaseURL = firebaseURL;
    }

    @Override
    public void run() {
        System.out.println("started Replay");
        for (GameViewModel gameRevision : gameRevisions) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
           firebaseUtil.setValue(firebaseURL, gameRevision);
        }
    }
}
