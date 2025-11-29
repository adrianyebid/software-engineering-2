package com.gymapp.service.core;

import com.gymapp.dto.request.auth.ChangeLoginRequestDTO;
import com.gymapp.dto.request.auth.LoginRequestDTO;

public interface UserSecurityHandler {
    boolean supports(String username);
    void authenticate(LoginRequestDTO dto);
    void changePassword(ChangeLoginRequestDTO dto);
}
