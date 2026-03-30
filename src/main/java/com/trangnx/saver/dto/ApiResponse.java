package com.trangnx.saver.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ApiResponse<T> {

    private LocalDateTime timestamp;
    private boolean success;
    private String message;
    private T data;
    private ErrorDetails error;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder().timestamp(LocalDateTime.now()).success(true).data(data).build();
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder().timestamp(LocalDateTime.now()).success(true).message(message).data(data).build();
    }

    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder().timestamp(LocalDateTime.now()).success(true).message(message).build();
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return ApiResponse.<T>builder().timestamp(LocalDateTime.now()).success(false)
                .error(new ErrorDetails(code, message, null)).build();
    }

    public static <T> ApiResponse<T> error(String code, String message, String detail) {
        return ApiResponse.<T>builder().timestamp(LocalDateTime.now()).success(false)
                .error(new ErrorDetails(code, message, detail)).build();
    }

    public static <T> ApiResponse<T> error(String message, ErrorDetails errorDetails) {
        return ApiResponse.<T>builder().timestamp(LocalDateTime.now()).success(false)
                .message(message).error(errorDetails).build();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorDetails {
        private String code;
        private String message;
        private String detail;
    }
}
