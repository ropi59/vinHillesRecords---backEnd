package fr.insy2s.commerce.exceptions;

public class CommandRequestExceptions extends RuntimeException {

    public CommandRequestExceptions(String message) {
        super(message);
    }

    public CommandRequestExceptions(String message, Throwable cause) {
        super(message, cause);
    }
}
