package com.gymapp.service.core;

import com.gymapp.dto.request.auth.ChangeLoginRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserPasswordManagerTest {

    private UserSecurityHandler handler;
    private UserPasswordManager passwordManager;

    @BeforeEach
    void setUp() {
        handler = mock(UserSecurityHandler.class);
        passwordManager = new UserPasswordManager(List.of(handler));
    }

    @Test
    void shouldChangePasswordWhenHandlerSupportsUser() {
        ChangeLoginRequestDTO dto = new ChangeLoginRequestDTO("sofia.ramirez", "old123", "new456");

        when(handler.supports("sofia.ramirez")).thenReturn(true);
        doNothing().when(handler).changePassword(dto);

        assertDoesNotThrow(() -> passwordManager.changePassword(dto));
        verify(handler).changePassword(dto);
    }

    @Test
    void shouldThrowExceptionWhenNoHandlerSupportsUser() {
        ChangeLoginRequestDTO dto = new ChangeLoginRequestDTO("unknown.user", "old", "new");

        when(handler.supports("unknown.user")).thenReturn(false);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> passwordManager.changePassword(dto));
        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    @Test
    void shouldUseCorrectHandlerAmongMultiple() {
        ChangeLoginRequestDTO dto = new ChangeLoginRequestDTO("miguel.torres", "oldpass", "newpass");

        UserSecurityHandler handler1 = mock(UserSecurityHandler.class);
        UserSecurityHandler handler2 = mock(UserSecurityHandler.class);

        when(handler1.supports("miguel.torres")).thenReturn(false);
        when(handler2.supports("miguel.torres")).thenReturn(true);
        doNothing().when(handler2).changePassword(dto);

        UserPasswordManager manager = new UserPasswordManager(List.of(handler1, handler2));

        assertDoesNotThrow(() -> manager.changePassword(dto));
        verify(handler2).changePassword(dto);
        verify(handler1, never()).changePassword(any());
    }
}