package fr.insy2s.commerce.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
@Data
public class CommandExceptions {
    private final String message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;

    public CommandExceptions(String message,
                             HttpStatus httpStatus,
                             ZonedDateTime timestamp) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }
}
