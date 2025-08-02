package com.example.bank_commission_service.service.discount;

import com.example.bank_commission_service.config.commission.CommissionProperties;
import com.example.bank_commission_service.exception.customException.ss.InvalidTransactionAmountException;
import com.example.bank_commission_service.model.transaction.Transaction;
import com.example.bank_commission_service.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommissionCalculatorTest {

    @InjectMocks
    private CommissionCalculator calculator;

    @Mock
    private CommissionProperties commissionProperties;

    @Mock
    private CommissionDiscountPolicy policy1;

    @Mock
    private CommissionDiscountPolicy policy2;

    @Mock
    private Transaction transaction;

    @Mock
    private User user;

    @BeforeEach
    void setUp() {
        calculator = new CommissionCalculator(commissionProperties, List.of(policy1, policy2));
    }

    @Test
    void calculate_shouldApplyAllDiscountPoliciesAndRoundUp() {
        mockTransactionAmount("1234567");
        mockBaseFeePercentage("0.01");
        mockMaxFee("100000");

        BigDecimal afterFirstPolicy = new BigDecimal("11111.103");
        BigDecimal afterSecondPolicy = new BigDecimal("10555.54785");

        when(policy1.apply(any(), eq(transaction))).thenReturn(afterFirstPolicy);
        when(policy2.apply(eq(afterFirstPolicy), eq(transaction))).thenReturn(afterSecondPolicy);

        BigDecimal result = calculator.calculate(transaction);

        assertEquals(afterSecondPolicy, result);
    }

    @Test
    void calculate_shouldNotExceedMaxFee() {
        mockTransactionAmount("999999999");
        mockBaseFeePercentage("0.05");
        mockMaxFee("100000");

        BigDecimal cappedFee = new BigDecimal("100000");

        when(policy1.apply(cappedFee, transaction)).thenReturn(cappedFee);
        when(policy2.apply(cappedFee, transaction)).thenReturn(cappedFee);

        BigDecimal result = calculator.calculate(transaction);

        assertEquals(cappedFee, result);
    }

    @Test
    void calculate_shouldReturnZeroIfPolicyReturnsZero() {
        mockTransactionAmount("1000");
        mockBaseFeePercentage("0.01");
        mockMaxFee("1000");

        when(policy1.apply(any(), any())).thenReturn(BigDecimal.ZERO);
        when(policy2.apply(BigDecimal.ZERO, transaction)).thenReturn(BigDecimal.ZERO);

        BigDecimal result = calculator.calculate(transaction);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void calculate_shouldCalculateBaseFeeWhenNoPolicies() {
        calculator = new CommissionCalculator(commissionProperties, Collections.emptyList());
        mockTransactionAmount("200");
        mockBaseFeePercentage("0.05"); // baseFee=10
        mockMaxFee("1000");

        BigDecimal result = calculator.calculate(transaction);

        assertEquals(new BigDecimal("10.00"), result);
    }

    @Test
    void calculate_shouldCapFeeByMaxFee() {
        mockTransactionAmount("1000");
        mockBaseFeePercentage("0.10"); // baseFee=100
        mockMaxFee("50");

        when(policy1.apply(new BigDecimal("50"), transaction)).thenReturn(new BigDecimal("50"));
        when(policy2.apply(new BigDecimal("50"), transaction)).thenReturn(new BigDecimal("50"));

        BigDecimal result = calculator.calculate(transaction);

        assertEquals(new BigDecimal("50"), result);
    }

    @Test
    void calculate_shouldAllowPolicyToIncreaseFee() {
        mockTransactionAmount("100");
        mockBaseFeePercentage("0.10"); // baseFee=10
        mockMaxFee("1000");

        when(policy1.apply(new BigDecimal("10.00"), transaction)).thenReturn(new BigDecimal("15.00"));
        when(policy2.apply(new BigDecimal("15.00"), transaction)).thenReturn(new BigDecimal("20.00"));

        BigDecimal result = calculator.calculate(transaction);

        assertEquals(new BigDecimal("20.00"), result);
    }

    @Test
    void calculate_shouldSkipNullPolicies() {
        calculator = new CommissionCalculator(commissionProperties, Arrays.asList(policy1, null, policy2));
        mockTransactionAmount("100");
        mockBaseFeePercentage("0.10"); // baseFee=10
        mockMaxFee("1000");

        when(policy1.apply(new BigDecimal("10.00"), transaction)).thenReturn(new BigDecimal("9.00"));
        when(policy2.apply(new BigDecimal("9.00"), transaction)).thenReturn(new BigDecimal("8.00"));

        BigDecimal result = calculator.calculate(transaction);

        assertEquals(new BigDecimal("8.00"), result);
    }

    @Test
    void calculate_shouldReturnZeroIfTransactionAmountIsZero() {
        mockTransactionAmount("0");

        assertThrows(InvalidTransactionAmountException.class, () -> calculator.calculate(transaction));
    }

    @Test
    void calculate_shouldReturnZeroIfTransactionAmountIsNegative() {
        mockTransactionAmount("-1");

        assertThrows(InvalidTransactionAmountException.class, () -> calculator.calculate(transaction));
    }

    private void mockTransactionAmount(String value) {
        lenient().when(transaction.getAmount()).thenReturn(new BigDecimal(value));
        lenient().when(transaction.getUser()).thenReturn(user);
    }

    private void mockBaseFeePercentage(String value) {
        lenient().when(commissionProperties.getBaseFeePercentage()).thenReturn(new BigDecimal(value));
    }

    private void mockMaxFee(String value) {
        lenient().when(commissionProperties.getMaxFee()).thenReturn(new BigDecimal(value));
    }
}