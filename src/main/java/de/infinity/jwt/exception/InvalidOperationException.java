package de.infinity.jwt.exception;

public class InvalidOperationException extends RuntimeException {
    private String message;

    public InvalidOperationException(String message) {
        super(message);
    }
}
