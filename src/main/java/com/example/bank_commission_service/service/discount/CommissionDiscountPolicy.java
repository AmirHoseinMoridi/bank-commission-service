package com.example.bank_commission_service.service.discount;

import com.example.bank_commission_service.model.transaction.Transaction;

import java.math.BigDecimal;

public interface CommissionDiscountPolicy {
    BigDecimal apply(BigDecimal currentFee, Transaction transaction);
}
