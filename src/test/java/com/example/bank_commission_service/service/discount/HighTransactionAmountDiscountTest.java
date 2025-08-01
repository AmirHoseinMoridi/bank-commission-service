package com.example.bank_commission_service.service.discount;

import com.example.bank_commission_service.config.commission.CommissionProperties;
import com.example.bank_commission_service.model.transaction.Transaction;
import com.example.bank_commission_service.model.user.User;
import com.example.bank_commission_service.model.user.UserTier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HighTransactionAmountDiscountTest {

    @InjectMocks
    private HighTransactionAmountDiscount discount;

    @Mock
    private CommissionProperties commissionProperties;
    private static final BigDecimal CURRENT_FEE = new BigDecimal(10_000);

    @BeforeEach
    void setup() {
        lenient().when(commissionProperties.getHighAmountThreshold())
                .thenReturn(new BigDecimal(1_000_000));

        lenient().when(commissionProperties.getHighAmountDiscountRate())
                .thenReturn(new BigDecimal("0.05"));
    }

    @Test
    void shouldApplyDiscountForTransactionAboveThreshold() {
        Transaction transaction = createTransaction(new BigDecimal(2_000_000));

        BigDecimal result = discount.apply(CURRENT_FEE, transaction);

        BigDecimal expected = CURRENT_FEE.multiply(BigDecimal.valueOf(0.95));
        assertEquals(expected, result);
    }

    @Test
    void shouldNotApplyDiscountForTransactionBelowThreshold() {
        Transaction transaction = createTransaction(new BigDecimal(999_999));

        BigDecimal result = discount.apply(CURRENT_FEE, transaction);

        assertEquals(CURRENT_FEE, result);
    }

    @Test
    void shouldNotApplyDiscountWhenAmountEqualsThreshold() {
        Transaction transaction = createTransaction(new BigDecimal(1_000_000));

        BigDecimal result = discount.apply(CURRENT_FEE, transaction);

        assertEquals(CURRENT_FEE, result);
    }

    private Transaction createTransaction(BigDecimal amount) {
        User user = new User();
        return Transaction.builder()
                .user(user)
                .amount(amount)
                .destinationAccountNumber("1234")
                .build();
    }
}