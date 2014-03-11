package be.kdg.spacecrack.controllers;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import be.kdg.spacecrack.Exceptions.SpaceCrackNotAcceptableException;
import be.kdg.spacecrack.services.IGameService;
import be.kdg.spacecrack.viewmodels.GameViewModel;
import be.kdg.spacecrack.viewmodels.RevisionListViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/auth/replay/{gameId}")
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
    public RevisionListViewModel getRevisionNumbers(@PathVariable final String gameId) {
        try {
            List<Integer> revisionNumbers = gameService.getRevisionNumbers(Integer.parseInt(gameId));

            RevisionListViewModel revisionListViewModel = new RevisionListViewModel();
            revisionListViewModel.setRevisions(revisionNumbers);
            return revisionListViewModel;
        } catch (NumberFormatException numberFormatException) {
            throw new SpaceCrackNotAcceptableException("Invalid number format for pathvariable gameId");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{revisionNumber}")
    @ResponseBody
    public GameViewModel getRevision(@PathVariable("gameId") final String gameId, @PathVariable("revisionNumber") final String revisionNumber) {
        int revisionNumberInt;
        try {
            revisionNumberInt = Integer.parseInt(revisionNumber);
        } catch (NumberFormatException numberFormatException) {
            throw new SpaceCrackNotAcceptableException("Invalid number format for pathvariable revisionNumber");
        }

        int gameIdInt;
        try {
            gameIdInt = Integer.parseInt(gameId);
        } catch (NumberFormatException numberFormatException) {
            throw new SpaceCrackNotAcceptableException("Invalid number format for pathvariable gameId");
        }

        return gameService.getGameRevisionByNumber(gameIdInt, revisionNumberInt);

    }

}
