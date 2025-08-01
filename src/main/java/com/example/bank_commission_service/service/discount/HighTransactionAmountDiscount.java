package com.example.bank_commission_service.service.discount;

import com.example.bank_commission_service.config.commission.CommissionProperties;
import com.example.bank_commission_service.model.transaction.Transaction;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor()
public class HighTransactionAmountDiscount implements CommissionDiscountPolicy {

    CommissionProperties commissionProperties;

    @Override
    public BigDecimal apply(BigDecimal currentFee, Transaction transaction) {
        BigDecimal threshold = commissionProperties.getHighAmountThreshold();
        if (transaction.getAmount().compareTo(threshold) > 0) {
            BigDecimal discountRate = BigDecimal.ONE.subtract(
                    commissionProperties.getHighAmountDiscountRate()
            );
            return currentFee.multiply(discountRate);
        }
        return currentFee;
    }
}
