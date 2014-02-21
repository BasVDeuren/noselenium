package be.kdg.spacecrack.Exceptions;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT)
public class SpaceCrackVictoryException extends RuntimeException{
    public SpaceCrackVictoryException(){
        super("Victory!");
    }
}
