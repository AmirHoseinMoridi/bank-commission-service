package com.example.bank_commission_service.controller;

import com.example.bank_commission_service.dto.AuthResultDto;
import com.example.bank_commission_service.dto.LogInParam;
import com.example.bank_commission_service.dto.RegisterParam;
import com.example.bank_commission_service.exception.customException.ss.UserAuthenticationException;
import com.example.bank_commission_service.exception.customException.ss.UserRegisterException;
import com.example.bank_commission_service.service.ServiceRegistry;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    ServiceRegistry serviceRegistry;

    @PostMapping("/logIn")
    public ResponseEntity<AuthResultDto> logIn(@RequestBody LogInParam logInParam) throws UserAuthenticationException {
        return ResponseEntity.ok(serviceRegistry.getUserService().login(logInParam));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResultDto> register(@RequestBody RegisterParam registerParam) throws UserRegisterException {
        return ResponseEntity.ok(serviceRegistry.getUserService().register(registerParam));
    }
}
