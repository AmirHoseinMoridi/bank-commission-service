package com.example.bank_commission_service.exception;

import com.example.bank_commission_service.exception.customException.ss.InvalidTransactionAmountException;
import org.springframework.http.ResponseEntity;
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
}
