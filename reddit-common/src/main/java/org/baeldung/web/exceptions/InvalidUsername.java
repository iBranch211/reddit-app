package org.baeldung.web.exceptions;

public class InvalidUsername extends RuntimeException {

    public InvalidUsername(final String message) {
        super(message);
    }

    public InvalidUsername(final String message, final Throwable cause) {
        super(message, cause);
    }
}
