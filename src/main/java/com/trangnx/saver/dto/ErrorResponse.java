package com.trangnx.saver.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode @ToString
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<String> details;

    public static ErrorResponse of(int status, String error, String message, String path) {
        return ErrorResponse.builder().timestamp(LocalDateTime.now()).status(status)
                .error(error).message(message).path(path).build();
    }

    public static ErrorResponse of(int status, String error, String message, String path, List<String> details) {
        return ErrorResponse.builder().timestamp(LocalDateTime.now()).status(status)
                .error(error).message(message).path(path).details(details).build();
    }
}
