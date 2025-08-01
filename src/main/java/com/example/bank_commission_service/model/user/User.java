package com.example.bank_commission_service.model.user;

import com.example.bank_commission_service.model.base.BaseEntity;
import com.example.bank_commission_service.model.transaction.Transaction;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User extends BaseEntity<Long> {

    @Column(unique = true, nullable = false)
    String username;

    @Column(nullable = false)
    String password;

    String firstName;

    String lastName;

    String email;

    @Column(nullable = false)
    String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    UserTier userTier = UserTier.REGULAR;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

}
