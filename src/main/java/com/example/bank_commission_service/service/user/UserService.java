package com.example.bank_commission_service.service.user;

import com.example.bank_commission_service.dto.LogInParam;
import com.example.bank_commission_service.dto.AuthResultDto;
import com.example.bank_commission_service.dto.RegisterParam;
import com.example.bank_commission_service.exception.customException.ss.DuplicateResourceException;
import com.example.bank_commission_service.exception.customException.ss.UserAuthenticationException;
import com.example.bank_commission_service.model.user.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);

    AuthResultDto login(LogInParam logInParam) throws UserAuthenticationException;

    AuthResultDto register(RegisterParam registerParam) throws DuplicateResourceException;

    boolean existsByEmail(String email);
}
