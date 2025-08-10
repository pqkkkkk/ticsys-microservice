package org.pqkkkkk.ticsys.event_service.exception;

public class UserIsNotValidException extends RuntimeException {
    public UserIsNotValidException(String message){
        super(message);
    }
    public UserIsNotValidException(String message, Throwable cause){
        super(message, cause);
    }
}
