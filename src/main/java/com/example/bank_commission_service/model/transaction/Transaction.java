package com.example.bank_commission_service.model.transaction;

import com.example.bank_commission_service.model.base.BaseEntity;
import com.example.bank_commission_service.model.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Transaction extends BaseEntity<Long> {

    @Column(nullable = false)
    String destinationAccountNumber;

    @Column(nullable = false)
    BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;
}
