package org.pqkkkkk.identity_service.exception;

public class ExistedUserException extends RuntimeException {
    public ExistedUserException(String message) {
        super(message);
    }
}
