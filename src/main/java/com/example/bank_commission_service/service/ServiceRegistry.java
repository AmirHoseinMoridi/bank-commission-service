package com.example.bank_commission_service.service;

import com.example.bank_commission_service.service.user.RoleService;
import com.example.bank_commission_service.service.user.UserService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Getter
@Component
public class ServiceRegistry {
    UserService userService;
    RoleService roleService;
}
