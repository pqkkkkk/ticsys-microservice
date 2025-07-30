package org.pqkkkkk.ticsys.identity_service.exception.handler;

import org.pqkkkkk.ticsys.identity_service.api.Response.ApiResponse;
import org.pqkkkkk.ticsys.identity_service.exception.InValidGoogleIdTokenException;
import org.pqkkkkk.ticsys.identity_service.exception.InvalidOTPException;
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

    @ExceptionHandler(InvalidOTPException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidOTPException(InvalidOTPException ex) {
        ApiResponse<String> response = new ApiResponse<>(null, false, HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
