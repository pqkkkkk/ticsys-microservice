package org.pqkkkkk.ticsys.event_service.exception;

public class NotEnoughInfoException extends RuntimeException {
    public NotEnoughInfoException(String message) {
        super(message);
    }

    public NotEnoughInfoException(String message, Throwable cause) {
        super(message, cause);
    }

}
