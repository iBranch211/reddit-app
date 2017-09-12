package org.baeldung.web.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException(final String message) {
        super(message);
    }

    public UsernameAlreadyExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
