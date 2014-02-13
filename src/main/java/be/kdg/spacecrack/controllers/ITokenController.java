package be.kdg.spacecrack.controllers;

import be.kdg.spacecrack.model.AccessToken;
import be.kdg.spacecrack.model.User;
import org.springframework.web.bind.annotation.*;

/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */
public interface ITokenController {
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    AccessToken login(@RequestBody User user);

    @RequestMapping(method = RequestMethod.DELETE, consumes = "application/json")
    void Logout(@RequestHeader("token") String tokenjson) throws Exception;
}
