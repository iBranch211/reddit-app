package org.baeldung.web.exceptions;

public class FeedServerException extends RuntimeException {

    public FeedServerException(String message) {
        super(message);
    }

    public FeedServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
