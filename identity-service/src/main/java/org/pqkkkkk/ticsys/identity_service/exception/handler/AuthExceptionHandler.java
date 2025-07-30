package org.pqkkkkk.ticsys.identity_service.exception.handler;

import org.pqkkkkk.ticsys.identity_service.api.Response.ApiError;
import org.pqkkkkk.ticsys.identity_service.exception.ExistedUserException;
import org.pqkkkkk.ticsys.identity_service.exception.UserNotFoundException;
import org.pqkkkkk.ticsys.identity_service.exception.WrongPasswordException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "org.pqkkkkk.ticsys.identity_service")
@Order(1)
public class AuthExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex) {
        ApiError apiError = new ApiError("User not found", HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<ApiError> handleWrongPasswordException(WrongPasswordException ex) {
        ApiError apiError = new ApiError("Wrong password", HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }

    @ExceptionHandler(ExistedUserException.class)
    public ResponseEntity<ApiError> handleExistedUserException(ExistedUserException ex) {
        ApiError apiError = new ApiError("User already exists", HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

    @ExceptionHandler(InvalidBearerTokenException.class)
    public ResponseEntity<ApiError> handleInvalidBearerTokenException(InvalidBearerTokenException ex) {
        ApiError apiError = new ApiError("Invalid token", HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }
}
