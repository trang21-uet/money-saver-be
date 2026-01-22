package com.trangnx.saver.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private boolean success;

    private String message;

    private T data;

    private ErrorDetails error;

    // Success response with data
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .success(true)
                .data(data)
                .build();
    }

    // Success response with data and message
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    // Success response with only message (no data)
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .success(true)
                .message(message)
                .build();
    }

    // Error response
    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .success(false)
                .message(message)
                .error(new ErrorDetails(errorCode, null))
                .build();
    }

    // Error response with details
    public static <T> ApiResponse<T> error(String message, String errorCode, String details) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .success(false)
                .message(message)
                .error(new ErrorDetails(errorCode, details))
                .build();
    }

    // Error response with ErrorDetails object
    public static <T> ApiResponse<T> error(String message, ErrorDetails errorDetails) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .success(false)
                .message(message)
                .error(errorDetails)
                .build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ErrorDetails {
        private String code;
        private String details;
    }
}