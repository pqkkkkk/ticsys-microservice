package org.pqkkkkk.ticsys.event_service.dto;

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
