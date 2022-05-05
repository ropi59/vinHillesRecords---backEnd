package fr.insy2s.commerce.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class CommandExceptionsHandler {

    @ExceptionHandler(value={CommandRequestExceptions.class})
    public ResponseEntity<Object> handleCommandRequestException(CommandRequestExceptions e){
        //1. create payload containing exception details
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        CommandExceptions commandExceptions = new CommandExceptions(
                e.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        //2. Return responseEntity
        return new ResponseEntity<>(commandExceptions,
                badRequest);
    }
}
