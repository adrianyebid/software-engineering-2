package com.gymapp.service.core;

import com.gymapp.dto.request.auth.LoginRequestDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthManager {

    private final List<UserSecurityHandler> handlers;

    public AuthManager(List<UserSecurityHandler> handlers) {
        this.handlers = handlers;
    }

    public void authenticate(LoginRequestDTO dto) {
        for (UserSecurityHandler handler : handlers) {
            if (handler.supports(dto.username())) {
                handler.authenticate(dto);
                return;
            }
        }
        throw new IllegalArgumentException("Invalid username or password");
    }
}