package com.example.bank_commission_service.config.commission;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "commission")
public class CommissionProperties {

    BigDecimal baseFeePercentage;
    BigDecimal maxFee;
    BigDecimal vipDiscountRate;
    BigDecimal highAmountThreshold;
    BigDecimal highAmountDiscountRate;
}
