package org.pqkkkkk.identity_service.exception.handler;

import org.pqkkkkk.identity_service.api.Response.ApiResponse;
import org.pqkkkkk.identity_service.exception.InValidGoogleIdTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GoogleAuthExceptionHandler {
    @ExceptionHandler(InValidGoogleIdTokenException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidGoogleIdTokenException(InValidGoogleIdTokenException ex) {
        ApiResponse<String> response = new ApiResponse<>(null, false, HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

}
