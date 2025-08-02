package com.example.bank_commission_service.exception;

import com.example.bank_commission_service.dto.AuthResultDto;
import com.example.bank_commission_service.exception.customException.ss.DuplicateResourceException;
import com.example.bank_commission_service.exception.customException.ss.InvalidTransactionAmountException;
import com.example.bank_commission_service.exception.customException.ss.UserAuthenticationException;
import com.example.bank_commission_service.exception.customException.ss.UserRegisterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidTransactionAmountException.class)
    public ResponseEntity<Map<String, String>> handleInvalidTransactionAmount(InvalidTransactionAmountException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Invalid transaction amount");
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UserRegisterException.class)
    public ResponseEntity<AuthResultDto> userRegisterException(UserRegisterException ex) {
        return
                new ResponseEntity<>(
                        new AuthResultDto(HttpStatus.BAD_REQUEST.toString(), ex.getMessage()),
                        HttpStatus.BAD_REQUEST
                );
    }

    @ExceptionHandler(UserAuthenticationException.class)
    public ResponseEntity<AuthResultDto> userAuthenticationException(UserAuthenticationException ex) {
        return new
                ResponseEntity<>(
                new AuthResultDto(HttpStatus.UNAUTHORIZED.toString(), ex.getMessage()),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<AuthResultDto> duplicateResourceException(DuplicateResourceException ex) {
        return new
                ResponseEntity<>(
                new AuthResultDto(HttpStatus.CONFLICT.toString(), ex.getMessage()),
                HttpStatus.CONFLICT
        );
    }
}
