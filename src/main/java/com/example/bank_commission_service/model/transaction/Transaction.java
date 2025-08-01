package com.example.bank_commission_service.model.transaction;

import com.example.bank_commission_service.model.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Transaction extends BaseEntity<Long> {

    @Column(nullable = false)
    String destinationAccountNumber;

    @Column(nullable = false)
    BigDecimal amount;
}
