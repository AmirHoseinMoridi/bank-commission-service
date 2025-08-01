package com.example.bank_commission_service.service.discount;

import com.example.bank_commission_service.config.commission.CommissionProperties;
import com.example.bank_commission_service.exception.customException.ss.InvalidTransactionAmountException;
import com.example.bank_commission_service.model.transaction.Transaction;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor()
public class CommissionCalculator {

    CommissionProperties commissionProperties;
    List<CommissionDiscountPolicy> policies;

    public BigDecimal calculate(Transaction transaction) {
        BigDecimal amount = transaction.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionAmountException("Transaction amount must be greater than zero.");
        }

        BigDecimal baseFee = transaction.getAmount().multiply(commissionProperties.getBaseFeePercentage());
        BigDecimal finalFee = baseFee.min(commissionProperties.getMaxFee());

        for (CommissionDiscountPolicy policy : policies) {
            if (policy != null)
                finalFee = policy.apply(finalFee, transaction);
        }

        return finalFee;
    }

}
