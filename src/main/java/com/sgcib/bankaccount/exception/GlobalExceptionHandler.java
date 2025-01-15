package com.sgcib.bankaccount.exception;

import com.sgcib.bankaccount.dto.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidOperationException(InvalidOperationException ex) {
        log.error("Invalid operation: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(
                createErrorResponse(ex.getMessage(), "INVALID_OPERATION", HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponseDTO> handleInsufficientFundsException(InsufficientFundsException ex) {
        log.error("Insufficient funds: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(
                createErrorResponse(ex.getMessage(), "INSUFFICIENT_FUNDS", HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleUnexpectedException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        return new ResponseEntity<>(
                createErrorResponse(
                        "An unexpected error occurred",
                        "INTERNAL_ERROR",
                        HttpStatus.INTERNAL_SERVER_ERROR
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private ErrorResponseDTO createErrorResponse(String message, String code, HttpStatus status) {
        return ErrorResponseDTO.builder()
                .message(message)
                .code(code)
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .build();
    }
}