package com.gymapp.controller;

import com.gymapp.dto.request.auth.ChangeLoginRequestDTO;
import com.gymapp.dto.request.auth.LoginRequestDTO;
import com.gymapp.service.core.AuthManager;
import com.gymapp.service.core.UserPasswordManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private AuthManager authManager;
    private UserPasswordManager userPasswordManager;
    private AuthController controller;

    @BeforeEach
    void setUp() {
        authManager = mock(AuthManager.class);
        userPasswordManager = mock(UserPasswordManager.class);
        controller = new AuthController(authManager, userPasswordManager);
    }

    @Test
    void shouldAuthenticateSuccessfully() {
        LoginRequestDTO dto = new LoginRequestDTO("ana.lopez", "1234");

        doNothing().when(authManager).authenticate(dto);

        var response = controller.login(dto);

        assertEquals(200, response.getStatusCode().value());
        verify(authManager).authenticate(dto);
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        ChangeLoginRequestDTO dto = new ChangeLoginRequestDTO("ana.lopez", "1234", "newpass");

        doNothing().when(userPasswordManager).changePassword(dto);

        var response = controller.changePassword(dto);

        assertEquals(200, response.getStatusCode().value());
        verify(userPasswordManager).changePassword(dto);
    }
}