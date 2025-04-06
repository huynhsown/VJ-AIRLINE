package com.vietjoke.vn.dto.response;

import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDTO {
    private int status;
    private String message;
    private List<ErrorDetail> errors;
    private String errorCode;
    private LocalDateTime timestamp;

    public ErrorResponseDTO(int status, String message, List<ErrorDetail> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponseDTO(int status, String message, List<ErrorDetail> errors, String errorCode) {
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class ErrorDetail {
        private String field;
        private String message;
        private String type;
    }
}
