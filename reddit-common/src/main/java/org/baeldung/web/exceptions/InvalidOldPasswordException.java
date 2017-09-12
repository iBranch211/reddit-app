package org.baeldung.web.exceptions;

public class InvalidOldPasswordException extends RuntimeException {

    public InvalidOldPasswordException(final String message) {
        super(message);
    }

    public InvalidOldPasswordException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
