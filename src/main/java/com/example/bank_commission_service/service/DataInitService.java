package com.example.bank_commission_service.service;

import com.example.bank_commission_service.model.user.Role;
import com.example.bank_commission_service.model.user.User;
import com.example.bank_commission_service.model.user.UserTier;
import com.example.bank_commission_service.repository.user.RoleRepository;
import com.example.bank_commission_service.repository.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
@Transactional
public class DataInitService {

    RoleRepository roleRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public DataInitService(RoleRepository roleRepository, UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        Role roleUser = new Role("ROLE_USER");
        roleRepository.save(roleUser);

        User regularUser = User.builder()
                .firstName("amir")
                .lastName("moridi")
                .email("amir.h.moridi@gmail.com")
                .username("amir111")
                .password(passwordEncoder.encode("password"))
                .roles(Set.of(roleUser))
                .userTier(UserTier.REGULAR)
                .accountNumber("1111")
                .build();
        userRepository.save(regularUser);

        User VipUser = User.builder()
                .firstName("erfan")
                .lastName("sarlak")
                .email("erfan@gmail.com")
                .username("erfan222")
                .password(passwordEncoder.encode("passwordErfan"))
                .roles(Set.of(roleUser))
                .userTier(UserTier.VIP)
                .accountNumber("2222")
                .build();
        userRepository.save(VipUser);
    }

}
