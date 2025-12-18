package com.insurance.insuranceApp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Standard error response")
public class ApiError {

    @Schema(example = "PRODUCT_NOT_FOUND")
    private String code;

    @Schema(example = "Product not found: 123")
    private String message;

    @Schema(example = "2025-12-18T10:15:30")
    private LocalDateTime timestamp = LocalDateTime.now();

    public ApiError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    // Add these getters
    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
