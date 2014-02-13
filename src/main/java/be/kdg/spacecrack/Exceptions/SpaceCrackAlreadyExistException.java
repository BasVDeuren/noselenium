package be.kdg.spacecrack.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Arno on 13/02/14.
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class SpaceCrackAlreadyExistException extends Exception {
    public SpaceCrackAlreadyExistException() {

    }
}
