package com.gymapp.service.core;

import com.gymapp.dto.request.auth.LoginRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthManager {

    private static final Logger log = LoggerFactory.getLogger(AuthManager.class);
    private final List<UserSecurityHandler> handlers;

    public AuthManager(List<UserSecurityHandler> handlers) {
        this.handlers = handlers;
    }

    public void authenticate(LoginRequestDTO dto) {
        log.info("Authenticating user: {}", dto.username());

        for (UserSecurityHandler handler : handlers) {
            if (handler.supports(dto.username())) {
                handler.authenticate(dto);
                log.info("Authentication successful for user: {}", dto.username());
                return;
            }
        }

        log.warn("Authentication failed: user {} not found", dto.username());
        throw new IllegalArgumentException("Invalid username or password");
    }
}