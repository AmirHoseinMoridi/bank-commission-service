package com.example.bank_commission_service.service.user.impl;

import com.example.bank_commission_service.dto.AuthResultDto;
import com.example.bank_commission_service.dto.LogInParam;
import com.example.bank_commission_service.dto.RegisterParam;
import com.example.bank_commission_service.exception.customException.ss.DuplicateResourceException;
import com.example.bank_commission_service.exception.customException.ss.UserAuthenticationException;
import com.example.bank_commission_service.model.user.Role;
import com.example.bank_commission_service.model.user.User;
import com.example.bank_commission_service.repository.user.UserRepository;
import com.example.bank_commission_service.security.JwtUtil;
import com.example.bank_commission_service.service.ServiceRegistry;
import com.example.bank_commission_service.service.user.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private ServiceRegistry serviceRegistry;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserServiceImpl userService;

    private final String USERNAME = "testuser";
    private final String EMAIL = "test@example.com";
    private final String PASSWORD = "password";
    private final String ENCODED_PASSWORD = "encodedPassword";
    private final String TOKEN = "generated.jwt.token";

    private User testUser;
    private Role userRole;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .username(USERNAME)
                .password(ENCODED_PASSWORD)
                .email(EMAIL)
                .build();

        userRole = new Role("ROLE_USER");
    }

    @Nested
    class FindByUsernameTests {

        @Test
        void whenUsernameExists_thenReturnUser() {
            given(userRepository.findByUsername(USERNAME)).willReturn(Optional.of(testUser));

            Optional<User> result = userService.findByUsername(USERNAME);

            assertThat(result).isPresent().contains(testUser);
            then(userRepository).should().findByUsername(USERNAME);
        }

        @Test
        void whenUsernameNotExists_thenReturnEmpty() {
            given(userRepository.findByUsername(USERNAME)).willReturn(Optional.empty());

            Optional<User> result = userService.findByUsername(USERNAME);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    class ExistsByEmailTests {

        @Test
        void whenEmailExists_thenReturnTrue() {
            given(userRepository.existsByEmail(EMAIL)).willReturn(true);

            boolean result = userService.existsByEmail(EMAIL);

            assertThat(result).isTrue();
        }

        @Test
        void whenEmailNotExists_thenReturnFalse() {
            given(userRepository.existsByEmail(EMAIL)).willReturn(false);

            boolean result = userService.existsByEmail(EMAIL);

            assertThat(result).isFalse();
        }
    }

    @Nested
    class LoginTests {

        private LogInParam validLoginParam;

        @BeforeEach
        void setUpLogin() {
            validLoginParam = new LogInParam(USERNAME, PASSWORD);
        }

        @Test
        void whenValidCredentials_thenReturnAuthResult() {
            given(userRepository.findByUsername(USERNAME)).willReturn(Optional.of(testUser));
            given(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).willReturn(true);
            given(jwtUtil.generateToken(USERNAME)).willReturn(TOKEN);

            AuthResultDto result = userService.login(validLoginParam);

            assertThat(result.getErrCode()).isEqualTo("200");
            assertThat(result.getErrMsg()).isEqualTo("Login successful");
            assertThat(result.getToken()).isEqualTo(TOKEN);
        }

        @Test
        void whenInvalidUsername_thenThrowException() {
            given(userRepository.findByUsername(USERNAME)).willReturn(Optional.empty());

            assertThatThrownBy(() -> userService.login(validLoginParam))
                    .isInstanceOf(UserAuthenticationException.class)
                    .hasMessage("Invalid username or password");
        }

        @Test
        void whenInvalidPassword_thenThrowException() {
            given(userRepository.findByUsername(USERNAME)).willReturn(Optional.of(testUser));
            given(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).willReturn(false);

            assertThatThrownBy(() -> userService.login(validLoginParam))
                    .isInstanceOf(UserAuthenticationException.class)
                    .hasMessage("Invalid username or password");
        }
    }

    @Nested
    class RegisterTests {

        private RegisterParam validRegisterParam;

        @BeforeEach
        void setUpRegister() {
            validRegisterParam = RegisterParam.builder()
                    .username(USERNAME)
                    .password(PASSWORD)
                    .email(EMAIL)
                    .firstName("Test")
                    .lastName("User")
                    .build();
        }

        @Test
        void whenValidInput_thenRegisterSuccessfully() {
            given(userRepository.existsByUsername(USERNAME)).willReturn(false);
            given(passwordEncoder.encode(PASSWORD)).willReturn(ENCODED_PASSWORD);
            given(roleService.findByName("ROLE_USER")).willReturn(Optional.of(userRole));
            given(serviceRegistry.getRoleService()).willReturn(roleService);

            AuthResultDto result = userService.register(validRegisterParam);

            assertThat(result.getErrCode()).isEqualTo("200");
            assertThat(result.getErrMsg()).isEqualTo("User successfully registered");

            then(userRepository).should().save(any(User.class));
            then(userRepository).should().existsByUsername(USERNAME);
        }

        @Test
        void whenUsernameExists_thenThrowException() {
            given(userRepository.existsByUsername(USERNAME)).willReturn(true);

            assertThatThrownBy(() -> userService.register(validRegisterParam))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessage("username has been taken");
        }

        @Test
        void whenRoleNotFound_thenThrowException() {
            given(userRepository.existsByUsername(USERNAME)).willReturn(false);
            given(roleService.findByName("ROLE_USER")).willReturn(Optional.empty());
            given(serviceRegistry.getRoleService()).willReturn(roleService);

            assertThatThrownBy(() -> userService.register(validRegisterParam))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Role USER not found");
        }
    }
}