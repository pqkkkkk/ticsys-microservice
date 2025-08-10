package org.pqkkkkk.ticsys.event_service.exception.handler;


import org.pqkkkkk.ticsys.event_service.dto.Response.ApiError;
import org.pqkkkkk.ticsys.event_service.dto.Response.ApiResponse;
import org.pqkkkkk.ticsys.event_service.exception.EventNotExistException;
import org.pqkkkkk.ticsys.event_service.exception.NotEnoughInfoException;
import org.pqkkkkk.ticsys.event_service.exception.UserIsNotValidException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(1)
@ControllerAdvice
public class EventExceptionHandler {
    @ExceptionHandler(EventNotExistException.class)
    public ResponseEntity<ApiResponse<Void>> handleEventNotExistException(EventNotExistException e) {
        log.error("Event not found: {}", e.getMessage());
        ApiError apiError = new ApiError("Event Not Found");
        ApiResponse<Void> response = new ApiResponse<>(null, false, HttpStatus.NOT_FOUND.value(),
            e.getMessage(),
            apiError
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(NotEnoughInfoException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotEnoughInfoException(NotEnoughInfoException e) {
        log.error("Not enough information provided: {}", e.getMessage());
        ApiError apiError = new ApiError(e.getMessage());
        ApiResponse<Void> response = new ApiResponse<>(null, false, HttpStatus.BAD_REQUEST.value(),
            e.getMessage(),
            apiError
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ExceptionHandler(UserIsNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserIsNotValidException(UserIsNotValidException e) {
        log.error("User is not valid: {}", e.getMessage());
        ApiError apiError = new ApiError(e.getMessage());
        ApiResponse<Void> response = new ApiResponse<>(null, false, HttpStatus.BAD_REQUEST.value(),
            e.getMessage(),
            apiError
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
