package be.kdg.spacecrack.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Atheesan on 9/02/14.
 */
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class SpaceCrackNotAcceptableException extends RuntimeException {
    public SpaceCrackNotAcceptableException(String message) {
        super(message);
    }
}
