package com.example.bank_commission_service.service.user.impl;

import com.example.bank_commission_service.model.user.Role;
import com.example.bank_commission_service.repository.user.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    public void findByName_WhenRoleExists_ShouldReturnRoleWhenRoleExists() {
        String roleName = "ADMIN";
        Role expectedRole = new Role(roleName);
        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(expectedRole));

        Optional<Role> result = roleService.findByName(roleName);

        assertTrue(result.isPresent());
        assertEquals(expectedRole, result.get());
        verify(roleRepository, times(1)).findByName(roleName);
    }

    @Test
    public void findByName_ShouldReturnEmptyOptionalWhenRoleDoesNotExist() {
        String roleName = "NON_EXISTENT_ROLE";
        when(roleRepository.findByName(roleName)).thenReturn(Optional.empty());

        Optional<Role> result = roleService.findByName(roleName);

        assertFalse(result.isPresent());
        verify(roleRepository, times(1)).findByName(roleName);
    }

    @Test
    public void findByName_ShouldThrowExceptionWhenNameIsNull() {
        when(roleRepository.findByName(null)).thenThrow(new IllegalArgumentException("Role name cannot be null"));

        assertThrows(IllegalArgumentException.class, () -> {
            roleService.findByName(null);
        });
        verify(roleRepository, times(1)).findByName(null);
    }
}