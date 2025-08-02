package com.example.bank_commission_service.security;

import com.example.bank_commission_service.service.ServiceRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    JwtUtil jwtUtil;
    ServiceRegistry serviceRegistry;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        if (email == null || email.isEmpty()) {
            sendErrorResponse(response, "Email not found in OAuth2 provider attributes", HttpStatus.BAD_REQUEST);
            return;
        }

        if (serviceRegistry.getUserService().existsByEmail(email)) {
            String token = jwtUtil.generateToken(email);

            sendSuccessResponse(response, token);
        } else {
            sendErrorResponse(response, "User not registered. Please sign up first.", HttpStatus.UNAUTHORIZED);
        }
    }

    private void sendSuccessResponse(HttpServletResponse response, String token) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write("{\"token\": \"" + token + "\"}");
        response.getWriter().flush();
    }

    private void sendErrorResponse(HttpServletResponse response, String message, HttpStatus status) throws IOException {
        response.setContentType("application/json");
        response.setStatus(status.value());
        response.getWriter().write("{\"error\": \"" + message + "\"}");
        response.getWriter().flush();
    }
}
