package com.sgcib.bankaccount.exception.error;

import com.sgcib.bankaccount.exception.BankException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private String code;
    private String message;
    private String path;
    private LocalDateTime timestamp;

    public static ApiError fromException(BankException ex, String path) {
        return ApiError.builder()
                .code(ex.getErrorCode().getCode())
                .message(ex.getMessage())
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
