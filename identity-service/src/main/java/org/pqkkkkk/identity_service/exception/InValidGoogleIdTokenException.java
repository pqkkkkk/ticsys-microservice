package org.pqkkkkk.identity_service.exception;

public class InValidGoogleIdTokenException extends RuntimeException {
    public InValidGoogleIdTokenException(String message) {
        super(message);
    }

    public InValidGoogleIdTokenException(String message, Throwable cause) {
        super(message, cause);
    }

}
