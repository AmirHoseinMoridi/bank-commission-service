package com.example.bank_commission_service.service.user;

import com.example.bank_commission_service.model.user.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(String name);
}
