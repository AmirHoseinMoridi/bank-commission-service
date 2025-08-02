package com.example.bank_commission_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionParam {

    @NotBlank()
    String destinationAccountNumber;

    @NotNull()
    @DecimalMin(value = "0.001")
    BigDecimal amount;
}
