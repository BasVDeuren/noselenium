package be.kdg.spacecrack.controllers;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.services.IGameService;
import be.kdg.spacecrack.utilities.FirebaseUtil;
import be.kdg.spacecrack.viewmodels.FirebaseURLViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/auth/replay/{playerId}")
public class ReplayController {


    @Autowired
    private IGameService gameService;


    public ReplayController() {
    }

    public ReplayController(IGameService gameService) {
        this.gameService = gameService;

    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public FirebaseURLViewModel getGameByGameId(@PathVariable final String playerId) {
        final String firebaseURL = FirebaseUtil.FIREBASEURLBASE + "oldGame/" + playerId;

        gameService.startReplay(Integer.parseInt(playerId), firebaseURL);

        System.out.println("THIS SHOULD HAPPEN FIRST");
        FirebaseURLViewModel firebaseURLViewModel = new FirebaseURLViewModel();
        firebaseURLViewModel.setFirebaseUrl(firebaseURL);
        return firebaseURLViewModel;
    }

}
