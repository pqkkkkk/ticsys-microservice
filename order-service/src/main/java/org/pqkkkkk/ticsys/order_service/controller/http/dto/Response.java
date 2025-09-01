package org.pqkkkkk.ticsys.order_service.controller.http.dto;

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
