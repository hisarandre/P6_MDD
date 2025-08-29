package com.openclassrooms.mddapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorValidationResponse {
    private String code;
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private Map<String, String> errors;

    public ErrorValidationResponse(String code, String message, int status, Map<String, String> errors) {
        this.code = code;
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.errors = errors;
    }
}
