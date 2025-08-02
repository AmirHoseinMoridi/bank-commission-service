package com.example.bank_commission_service.service.user.impl;

import com.example.bank_commission_service.model.user.Role;
import com.example.bank_commission_service.repository.user.RoleRepository;
import com.example.bank_commission_service.service.user.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }
}
