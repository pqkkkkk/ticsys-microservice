package org.pqkkkkk.ticsys.promotion_service.controller.rest.dto;

public class Response {
    public record ApiError(
        String error
    ){}
    public record ApiResponse<T>(
        T data,
        boolean success,
        int statusCode,
        String message,
        ApiError error
    ){}
}
