package org.baeldung.web.exceptions;

public class InvalidResubmitOptionsException extends RuntimeException {

    public InvalidResubmitOptionsException(String message) {
        super(message);
    }

    public InvalidResubmitOptionsException(String message, Throwable cause) {
        super(message, cause);
    }
}
