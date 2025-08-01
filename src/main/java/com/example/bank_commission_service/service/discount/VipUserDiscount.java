package com.example.bank_commission_service.service.discount;

import com.example.bank_commission_service.config.commission.CommissionProperties;
import com.example.bank_commission_service.model.transaction.Transaction;
import com.example.bank_commission_service.model.user.UserTier;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor()
public class VipUserDiscount implements CommissionDiscountPolicy {

    CommissionProperties commissionProperties;

    @Override
    public BigDecimal apply(BigDecimal currentFee, Transaction transaction) {
        UserTier tier = transaction.getUser().getUserTier();
        if (UserTier.VIP.equals(tier)) {
            BigDecimal discountRate = BigDecimal.ONE.subtract(commissionProperties.getVipDiscountRate());
            return currentFee.multiply(discountRate);
        }
        return currentFee;
    }
}
