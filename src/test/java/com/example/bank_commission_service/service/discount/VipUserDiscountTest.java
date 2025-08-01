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


@ExtendWith(MockitoExtension.class)
class VipUserDiscountTest {

    @InjectMocks
    private VipUserDiscount discount;

    @Mock
    private CommissionProperties commissionProperties;

    private static final BigDecimal CURRENT_FEE = new BigDecimal(10_000);

    @BeforeEach
    void setup() {
        lenient().when(commissionProperties.getVipDiscountRate())
                .thenReturn(new BigDecimal("0.10"));
    }

    @Test
    void shouldApplyDiscountForVipUser() {
        Transaction transaction = createTransaction(UserTier.VIP);

        BigDecimal result = discount.apply(CURRENT_FEE, transaction);

        BigDecimal expected = CURRENT_FEE.multiply(BigDecimal.valueOf(0.9)).setScale(2, RoundingMode.HALF_UP);
        assertEquals(expected, result);
    }

    @Test
    void shouldNotApplyDiscountForRegularUser() {
        Transaction transaction = createTransaction(UserTier.REGULAR);

        BigDecimal result = discount.apply(CURRENT_FEE, transaction);

        assertEquals(CURRENT_FEE, result);
    }

    @Test
    void shouldNotApplyDiscountWhenUserTierIsNull() {
        User user = new User();
        user.setUserTier(null);
        Transaction transaction = new Transaction();
        transaction.setUser(user);

        BigDecimal result = discount.apply(CURRENT_FEE, transaction);

        assertEquals(CURRENT_FEE, result);
    }

    private Transaction createTransaction(UserTier tier) {
        User user = new User();
        user.setUserTier(tier);
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        return transaction;
    }
}
