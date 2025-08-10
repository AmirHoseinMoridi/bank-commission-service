package com.example.bank_commission_service.service.user.impl;

import com.example.bank_commission_service.dto.LogInParam;
import com.example.bank_commission_service.dto.AuthResultDto;
import com.example.bank_commission_service.dto.RegisterParam;
import com.example.bank_commission_service.exception.customException.ss.DuplicateResourceException;
import com.example.bank_commission_service.exception.customException.ss.UserAuthenticationException;
import com.example.bank_commission_service.model.user.Role;
import com.example.bank_commission_service.model.user.User;
import com.example.bank_commission_service.repository.user.UserRepository;
import com.example.bank_commission_service.security.JwtUtil;
import com.example.bank_commission_service.service.ServiceRegistry;
import com.example.bank_commission_service.service.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    JwtUtil jwtUtil;
    ServiceRegistry serviceRegistry;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public AuthResultDto login(LogInParam logInParam) throws UserAuthenticationException {
        User user = userRepository.findByUsername(logInParam.getUsername())
                .orElseThrow(() -> new UserAuthenticationException("Invalid username or password"));

        if (!passwordEncoder.matches(logInParam.getPassword(), user.getPassword()))
            throw new UserAuthenticationException("Invalid username or password");

        String token = jwtUtil.generateToken(user.getUsername());

        return new AuthResultDto("200", "Login successful", token);
    }

    @Transactional
    @Override
    public AuthResultDto register(RegisterParam registerParam) throws DuplicateResourceException {

        if (userRepository.existsByUsername(registerParam.getUsername()))
            throw new DuplicateResourceException("username has been taken");

        User user = createUser(registerParam);

        Role userRole = serviceRegistry.getRoleService().findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role USER not found"));

        user.getRoles().add(userRole);

        userRepository.save(user);

        return new AuthResultDto("200", "User successfully registered");
    }

    private User createUser(RegisterParam registerParam) {
        return User.builder()
                .username(registerParam.getUsername())
                .password(passwordEncoder.encode(registerParam.getPassword()))
                .email(registerParam.getEmail())
                .firstName(registerParam.getFirstName())
                .lastName(registerParam.getLastName())
                .accountNumber(registerParam.getAccountNumber())
                .build();
    }

}
