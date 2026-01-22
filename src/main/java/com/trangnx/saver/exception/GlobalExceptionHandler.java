package com.trangnx.saver.exception;

import com.trangnx.saver.dto.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // JWT Token Exceptions
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<Void>> handleExpiredJwtException(
            ExpiredJwtException ex,
            HttpServletRequest request) {
        ApiResponse<Void> response = ApiResponse.error(
                "Token has expired. Please refresh your token or login again.",
                "TOKEN_EXPIRED"
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiResponse<Void>> handleSignatureException(
            SignatureException ex,
            HttpServletRequest request) {
        ApiResponse<Void> response = ApiResponse.error(
                "Invalid token signature. Token may have been tampered with.",
                "INVALID_SIGNATURE"
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ApiResponse<Void>> handleMalformedJwtException(
            MalformedJwtException ex,
            HttpServletRequest request) {
        ApiResponse<Void> response = ApiResponse.error(
                "Malformed token. Please provide a valid JWT token.",
                "MALFORMED_TOKEN"
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidTokenException(
            InvalidTokenException ex,
            HttpServletRequest request) {
        ApiResponse<Void> response = ApiResponse.error(
                ex.getMessage(),
                "INVALID_TOKEN"
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // Authentication & Authorization Exceptions
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(
            AuthenticationException ex,
            HttpServletRequest request) {
        ApiResponse<Void> response = ApiResponse.error(
                "Authentication failed: " + ex.getMessage(),
                "AUTHENTICATION_FAILED"
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(
            AccessDeniedException ex,
            HttpServletRequest request) {
        ApiResponse<Void> response = ApiResponse.error(
                "You don't have permission to access this resource.",
                "ACCESS_DENIED"
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(
            BadCredentialsException ex,
            HttpServletRequest request) {
        ApiResponse<Void> response = ApiResponse.error(
                "Invalid credentials. Please check your username and password.",
                "BAD_CREDENTIALS"
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request) {
        ApiResponse<Void> response = ApiResponse.error(
                ex.getMessage(),
                "RESOURCE_NOT_FOUND"
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Validation Exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        String details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ApiResponse<Void> response = ApiResponse.error(
                "Validation failed",
                "VALIDATION_ERROR",
                details
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {
        String message = String.format(
                "Parameter '%s' should be of type %s",
                ex.getName(),
                ex.getRequiredType().getSimpleName()
        );
        ApiResponse<Void> response = ApiResponse.error(
                message,
                "TYPE_MISMATCH"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {
        ApiResponse<Void> response = ApiResponse.error(
                ex.getMessage(),
                "ILLEGAL_ARGUMENT"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Generic Exception Handler (fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(
            Exception ex,
            HttpServletRequest request) {

        // Log the exception
        System.err.println("Unhandled exception: " + ex.getClass().getName());
        ex.printStackTrace();

        ApiResponse<Void> response = ApiResponse.error(
                "An unexpected error occurred. Please try again later.",
                "INTERNAL_ERROR",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}