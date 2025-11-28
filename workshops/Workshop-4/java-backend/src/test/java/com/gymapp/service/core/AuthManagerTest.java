package com.gymapp.service.core;

import com.gymapp.dto.request.auth.LoginRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthManagerTest {

    private UserSecurityHandler handler;
    private AuthManager authManager;

    @BeforeEach
    void setUp() {
        handler = mock(UserSecurityHandler.class);
        authManager = new AuthManager(List.of(handler));
    }

    @Test
    void shouldAuthenticateWhenHandlerSupportsUser() {
        LoginRequestDTO login = new LoginRequestDTO("sofia.ramirez", "secure123");

        when(handler.supports("sofia.ramirez")).thenReturn(true);
        doNothing().when(handler).authenticate(login);

        assertDoesNotThrow(() -> authManager.authenticate(login));
        verify(handler).authenticate(login);
    }

    @Test
    void shouldThrowExceptionWhenNoHandlerSupportsUser() {
        LoginRequestDTO login = new LoginRequestDTO("unknown.user", "pass");

        when(handler.supports("unknown.user")).thenReturn(false);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> authManager.authenticate(login));
        assertEquals("Invalid username or password", ex.getMessage());
    }

    @Test
    void shouldLogAndAuthenticateWithCorrectHandler() {
        LoginRequestDTO login = new LoginRequestDTO("trainer.juan", "pass");

        UserSecurityHandler handler1 = mock(UserSecurityHandler.class);
        UserSecurityHandler handler2 = mock(UserSecurityHandler.class);

        when(handler1.supports("trainer.juan")).thenReturn(false);
        when(handler2.supports("trainer.juan")).thenReturn(true);
        doNothing().when(handler2).authenticate(login);

        AuthManager manager = new AuthManager(List.of(handler1, handler2));

        assertDoesNotThrow(() -> manager.authenticate(login));
        verify(handler2).authenticate(login);
        verify(handler1, never()).authenticate(any());
    }
}