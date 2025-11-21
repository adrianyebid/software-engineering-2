package com.gymapp.interceptor;

import com.gymapp.dto.request.auth.LoginRequestDTO;
import com.gymapp.service.core.AuthManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AuthenticationInterceptorTest {

    private AuthManager authManager;
    private AuthenticationInterceptor interceptor;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        authManager = mock(AuthManager.class);
        interceptor = new AuthenticationInterceptor(authManager);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    void shouldAllowPublicPostEndpoint() {
        when(request.getRequestURI()).thenReturn("/api/v1/auth/login");
        when(request.getMethod()).thenReturn("POST");

        boolean result = interceptor.preHandle(request, response, new Object());

        assertTrue(result);
        verifyNoInteractions(authManager);
    }

    @Test
    void shouldRejectMissingCredentials() {
        when(request.getRequestURI()).thenReturn("/api/v1/sessions");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("X-Username")).thenReturn(null);
        when(request.getHeader("X-Password")).thenReturn(null);

        boolean result = interceptor.preHandle(request, response, new Object());

        assertFalse(result);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verifyNoInteractions(authManager);
    }

    @Test
    void shouldRejectInvalidCredentials() {
        when(request.getRequestURI()).thenReturn("/api/v1/sessions");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("X-Username")).thenReturn("user");
        when(request.getHeader("X-Password")).thenReturn("wrong");

        doThrow(new RuntimeException("Invalid")).when(authManager).authenticate(any(LoginRequestDTO.class));

        boolean result = interceptor.preHandle(request, response, new Object());

        assertFalse(result);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(authManager).authenticate(any(LoginRequestDTO.class));
    }

    @Test
    void shouldAllowValidCredentials() {
        when(request.getRequestURI()).thenReturn("/api/v1/sessions");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("X-Username")).thenReturn("validUser");
        when(request.getHeader("X-Password")).thenReturn("validPass");

        boolean result = interceptor.preHandle(request, response, new Object());

        assertTrue(result);
        verify(authManager).authenticate(new LoginRequestDTO("validUser", "validPass"));
    }
}