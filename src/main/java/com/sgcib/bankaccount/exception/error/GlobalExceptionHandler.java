package com.sgcib.bankaccount.exception.error;

import com.sgcib.bankaccount.exception.AccountNotFoundException;
import com.sgcib.bankaccount.exception.InsufficientFundsException;
import com.sgcib.bankaccount.exception.InvalidAmountException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiError> handleAccountNotFound(
            AccountNotFoundException ex) {
        ApiError error = ApiError.builder()
                .code(ErrorCode.ACCOUNT_NOT_FOUND.getCode())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<ApiError> handleInvalidAmount(
            InvalidAmountException ex, WebRequest request) {
        log.error("Invalid amount", ex);
        ApiError error = ApiError.fromException(ex, request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ApiError> handleInsufficientFunds(
            InsufficientFundsException ex, WebRequest request) {
        log.error("Insufficient funds", ex);
        ApiError error = ApiError.fromException(ex, request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllUncaughtException(
            Exception ex, WebRequest request) {
        log.error("Uncaught error occurred", ex);

        ApiError error = ApiError.builder()
                .code(ErrorCode.OPERATION_FAILED.getCode())
                .message("An unexpected error occurred")
                .path(request.getDescription(false))
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
