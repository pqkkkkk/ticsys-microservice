package org.pqkkkkk.ticsys.event_service.exception.handler;

import org.pqkkkkk.ticsys.event_service.dto.Response.ApiError;
import org.pqkkkkk.ticsys.event_service.dto.Response.ApiResponse;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.RollbackException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(2)
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataAccessException(DataAccessException e) {
        log.error("Data access error occurred: {}", e.getMessage());
        ApiError apiError = new ApiError("Data Access Error");
        ApiResponse <Void> response = new ApiResponse<>(null, false, HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An error occurred while accessing the data",
            apiError
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("Constraint violation: {}", e.getMessage());
        ApiError apiError = new ApiError(e.getMessage());
        ApiResponse<Void> response = new ApiResponse<>(null, false, HttpStatus.BAD_REQUEST.value(),
            "An error occurred due to constraint violation",
            apiError
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Invalid argument: {}", e.getMessage());
        ApiError apiError = new ApiError(e.getMessage());
        ApiResponse<Void> response = new ApiResponse<>(null, false, HttpStatus.BAD_REQUEST.value(),
            "An error occurred due to invalid argument",
            apiError
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(RollbackException.class)
    public ResponseEntity<ApiResponse<Void>> handleRollbackException(RollbackException e) {
        log.error("Transaction rolled back: {}", e.getMessage());
        ApiError apiError = new ApiError("Transaction failed");
        ApiResponse<Void> response = new ApiResponse<>(null, false, HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An error occurred during transaction rollback",
            apiError
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ApiResponse<Void>> handleTransactionSystemException(TransactionSystemException e) {
        log.error("Transaction system error: {}", e.getMessage());
        ApiError apiError = new ApiError("Transaction system error");
        ApiResponse<Void> response = new ApiResponse<>(null, false, HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An error occurred during transaction processing",
            apiError
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("Entity not found: {}", e.getMessage());
        ApiError apiError = new ApiError(e.getMessage());
        ApiResponse<Void> response = new ApiResponse<>(null, false, HttpStatus.NOT_FOUND.value(),
            "Entity not found",
            apiError
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<Void>> handleNullPointerException(NullPointerException e) {
        log.error("Null pointer exception: {}", e.getMessage());
        ApiError apiError = new ApiError("Null pointer exception");
        ApiResponse<Void> response = new ApiResponse<>(null, false, HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An error occurred due to null pointer",
            apiError
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Validation error: {}", e.getMessage());
        
        StringBuilder errorMessage = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            errorMessage.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        });
        
        ApiError apiError = new ApiError(errorMessage.toString());
        ApiResponse<Void> response = new ApiResponse<>(null, false, HttpStatus.BAD_REQUEST.value(),
            "Validation failed",
            apiError
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("An error occurred: {}", e.getMessage());
        ApiError apiError = new ApiError("Internal Server Error");
        ApiResponse<Void> response = new ApiResponse<>(null, false, HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An error occurred",
            apiError
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
