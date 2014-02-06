package be.kdg.spacecrack.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Ikke on 6-2-14.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class SpaceCrackUnexpectedException extends RuntimeException{
    public SpaceCrackUnexpectedException(String message) {
        super(message);
    }
}
