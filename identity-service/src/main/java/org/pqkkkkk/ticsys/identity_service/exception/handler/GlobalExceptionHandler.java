package org.pqkkkkk.ticsys.identity_service.exception.handler;

import org.pqkkkkk.ticsys.identity_service.api.Response.ApiError;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.RollbackException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
@Order(2)
public class GlobalExceptionHandler {

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiError> handleDataAccessException(DataAccessException e) {
        log.error("Data access error occurred: {}", e.getMessage());
        ApiError apiError = new ApiError("Data Access Error", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("Constraint violation: {}", e.getMessage());
        ApiError apiError = new ApiError("Constraint Violation", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Invalid argument: {}", e.getMessage());
        ApiError apiError = new ApiError("Invalid Argument", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
     public ResponseEntity<ApiError> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("Invalid argument: {}", e.getMessage());
        ApiError apiError = new ApiError("Invalid Argument", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    } 

    @ExceptionHandler(RollbackException.class)
    public ResponseEntity<ApiError> handleRollbackException(RollbackException e) {
        log.error("Transaction rolled back: {}", e.getMessage());
        ApiError apiError = new ApiError("Transaction failed", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ApiError> handleTransactionSystemException(TransactionSystemException e) {
        log.error("Transaction system error: {}", e.getMessage());
        ApiError apiError = new ApiError("Transaction system error", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("Entity not found: {}", e.getMessage());
        ApiError apiError = new ApiError(e.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationError(MethodArgumentNotValidException e){
        log.error("Validation error: {}", e.getMessage());
        ApiError apiError = new ApiError("Validation Error", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception e) {
        log.error("An error occurred: {}", e.getMessage());
        ApiError apiError = new ApiError("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

}
