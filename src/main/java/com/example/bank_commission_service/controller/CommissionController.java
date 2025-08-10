package com.example.bank_commission_service.controller;

import com.example.bank_commission_service.dto.TransactionParam;
import com.example.bank_commission_service.mapper.Mapper;
import com.example.bank_commission_service.model.transaction.Transaction;
import com.example.bank_commission_service.model.user.User;
import com.example.bank_commission_service.service.ServiceRegistry;
import com.example.bank_commission_service.service.discount.CommissionCalculator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@RequestMapping("/commission")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommissionController {

    CommissionCalculator calculator;
    ServiceRegistry serviceRegistry;
    Mapper mapper;

    @PostMapping("/calculate")
    public ResponseEntity<BigDecimal> calculateCommission(@RequestBody TransactionParam transactionParam,
                                                          Principal principal) {

        String username = principal.getName();
        User user = serviceRegistry.getUserService().findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Transaction transaction = mapper.paramToTransaction(transactionParam);
        transaction.setUser(user);

        return ResponseEntity.ok(calculator.calculate(transaction));
    }
}
