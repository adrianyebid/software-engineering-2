package com.gymapp.service.core;

import com.gymapp.dto.request.auth.ChangeLoginRequestDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPasswordManager {

    private final List<UserSecurityHandler> handlers;

    public UserPasswordManager(List<UserSecurityHandler> handlers) {
        this.handlers = handlers;
    }

    public void changePassword(ChangeLoginRequestDTO dto) {
        for (UserSecurityHandler handler : handlers) {
            if (handler.supports(dto.username())) {
                handler.changePassword(dto);
                return;
            }
        }
        throw new IllegalArgumentException("Usuario no encontrado");
    }
}