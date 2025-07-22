package org.pqkkkkk.identity_service.api;

public class Response {
    public record ApiError(
        String message,
        int statusCode
    ){}
    public record ApiResponse<T>(
        T data,
        boolean success,
        int statusCode,
        String message
    ){}
}
