package com.insurance.insuranceApp.exception;

import com.insurance.insuranceApp.dto.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle Access Denied exceptions (403 Forbidden)
     * Triggered when user tries to access another client's data
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(
            AccessDeniedException ex,
            WebRequest request) {

        log.warn("Access Denied: {}", ex.getMessage());

        ApiError error = new ApiError(
                "ACCESS_DENIED",
                ex.getMessage() != null ? ex.getMessage() : "You do not have permission to access this resource"
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    /**
     * Handle Authentication exceptions (401 Unauthorized)
     * JWT token is invalid, expired, or missing
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(
            AuthenticationException ex,
            WebRequest request) {

        log.warn("Authentication failed: {}", ex.getMessage());

        ApiError error = new ApiError(
                "AUTHENTICATION_FAILED",
                "Authentication failed: " + (ex.getMessage() != null ? ex.getMessage() : "Invalid credentials")
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    /**
     * Handle Username Not Found (401 Unauthorized)
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiError> handleUsernameNotFoundException(
            UsernameNotFoundException ex,
            WebRequest request) {

        log.warn("User not found: {}", ex.getMessage());

        ApiError error = new ApiError(
                "USER_NOT_FOUND",
                ex.getMessage() != null ? ex.getMessage() : "User not found"
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    /**
     * Handle Client Not Found (404 Not Found)
     */
    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ApiError> handleClientNotFound(
            ClientNotFoundException ex) {

        log.warn("Client not found: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError("CLIENT_NOT_FOUND", ex.getMessage()));
    }

    /**
     * Handle User Already Exists (409 Conflict)
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleUserAlreadyExistsException(
            UserAlreadyExistsException ex) {

        log.warn("User already exists: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError("USER_ALREADY_EXISTS", ex.getMessage()));
    }

    /**
     * Handle Product Not Found (404 Not Found)
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiError> handleProductNotFound(
            ProductNotFoundException ex) {

        log.warn("Product not found: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError("PRODUCT_NOT_FOUND", ex.getMessage()));
    }

    /**
     * Handle Authentication Failed (403 Forbidden)
     */
    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ApiError> handleAuthFailed(
            AuthenticationFailedException ex) {

        log.warn("Authentication failed: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiError("AUTH_FAILED", ex.getMessage()));
    }

    /**
     * Handle validation errors (400 Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        log.warn("Validation error: {}", ex.getMessage());

        String details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ApiError error = new ApiError(
                "VALIDATION_ERROR",
                "Validation failed: " + details
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handle all other unexpected exceptions (500 Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception ex) {

        log.error("Unexpected error: ", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError("INTERNAL_ERROR", "Unexpected server error"));
    }
}