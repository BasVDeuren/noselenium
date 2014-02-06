package be.kdg.spacecrack.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Tim on 6/02/14.
 */
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class InvalidTokenHeaderException extends RuntimeException {
    public InvalidTokenHeaderException() {
        super("Invalid Token");
    }
}
