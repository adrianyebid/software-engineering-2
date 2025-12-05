package com.gymapp.service.core;

import com.gymapp.dto.response.auth.AuthResponseDTO;
import com.gymapp.dto.response.auth.RefreshResponseDTO;
import com.gymapp.exception.TooManyRequestsException;
import com.gymapp.exception.UnauthorizedException;
import com.gymapp.model.RefreshToken;
import com.gymapp.service.security.BruteForceProtectionService;
import com.gymapp.service.security.CustomUserDetailsService;
import com.gymapp.service.security.JwtService;
import com.gymapp.service.security.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Unit Tests")
class AuthServiceTest {

    @Mock
    private AuthenticationManager authManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private BruteForceProtectionService bruteForceProtectionService;

    @InjectMocks
    private AuthService authService;

    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "password";
    private static final String ROLE = "ROLE_TRAINER";
    private static final String ACCESS_TOKEN = "access.token.123";
    private static final String REFRESH_TOKEN_VALUE = "refresh.token.456";

    private Authentication successfulAuth;
    private RefreshToken refreshToken;

    @BeforeEach
    void setUp() {
        GrantedAuthority authority = new SimpleGrantedAuthority(ROLE);
        successfulAuth = new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD, List.of(authority));

        refreshToken = new RefreshToken();
        refreshToken.setToken(REFRESH_TOKEN_VALUE);
        refreshToken.setUsername(USERNAME);
    }

    // ============================================================
    // LOGIN TESTS
    // ============================================================

    @Test
    void login_shouldReturnAuthResponseOnSuccessfulAuthentication() {
        when(bruteForceProtectionService.isBlocked(USERNAME)).thenReturn(false);
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(successfulAuth);
        when(jwtService.generateToken(eq(USERNAME), any())).thenReturn(ACCESS_TOKEN);
        when(refreshTokenService.createToken(USERNAME)).thenReturn(refreshToken);

        AuthResponseDTO response = authService.login(USERNAME, PASSWORD);

        assertNotNull(response);
        assertEquals(ACCESS_TOKEN, response.accessToken());
        assertEquals(REFRESH_TOKEN_VALUE, response.refreshToken());
        assertEquals(ROLE, response.role());
        assertEquals(AuthService.TOKEN_TYPE, response.tokenType());

        verify(bruteForceProtectionService).loginSucceeded(USERNAME);
        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void login_shouldThrowTooManyRequestsExceptionIfUserIsBlocked() {
        when(bruteForceProtectionService.isBlocked(USERNAME)).thenReturn(true);

        assertThrows(TooManyRequestsException.class, () -> authService.login(USERNAME, PASSWORD));

        verify(bruteForceProtectionService, never()).loginSucceeded(any());
        verify(authManager, never()).authenticate(any());
    }

    @Test
    void login_shouldHandleAuthenticationFailureAndThrowBadCredentials() {
        AuthenticationException authException = mock(AuthenticationException.class);
        when(bruteForceProtectionService.isBlocked(USERNAME)).thenReturn(false);
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(authException);

        BadCredentialsException thrown = assertThrows(BadCredentialsException.class,
                () -> authService.login(USERNAME, PASSWORD));

        assertEquals("Invalid credentials", thrown.getMessage());
        assertEquals(authException, thrown.getCause());
        verify(bruteForceProtectionService).loginFailed(USERNAME);
        verify(bruteForceProtectionService, never()).loginSucceeded(any());
    }

    @Test
    void login_shouldHandleUnknownRoleGracefully() {
        Authentication authWithoutRole = new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD, List.of());
        when(bruteForceProtectionService.isBlocked(USERNAME)).thenReturn(false);
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authWithoutRole);
        when(jwtService.generateToken(eq(USERNAME), any())).thenReturn(ACCESS_TOKEN);
        when(refreshTokenService.createToken(USERNAME)).thenReturn(refreshToken);

        AuthResponseDTO response = authService.login(USERNAME, PASSWORD);

        assertEquals(AuthService.UNKNOWN, response.role());
    }

    // ============================================================
    // REFRESH TESTS
    // ============================================================

    @Test
    void refresh_shouldReturnNewAccessTokenOnValidRefreshToken() {
        UserDetails userDetails = User.withUsername(USERNAME)
                .password(PASSWORD)
                .authorities(new SimpleGrantedAuthority(ROLE))
                .build();

        when(refreshTokenService.findByToken(REFRESH_TOKEN_VALUE)).thenReturn(Optional.of(refreshToken));
        when(customUserDetailsService.loadUserByUsername(USERNAME)).thenReturn(userDetails);
        when(jwtService.generateToken(eq(USERNAME), any())).thenReturn(ACCESS_TOKEN);

        RefreshResponseDTO response = authService.refresh(REFRESH_TOKEN_VALUE);

        assertNotNull(response);
        assertEquals(ACCESS_TOKEN, response.accessToken());
        assertEquals(AuthService.TOKEN_TYPE, response.tokenType());

        verify(jwtService).generateToken(eq(USERNAME), eq(userDetails.getAuthorities()));
    }

    @Test
    void refresh_shouldThrowUnauthorizedExceptionIfTokenNotFound() {
        when(refreshTokenService.findByToken(REFRESH_TOKEN_VALUE)).thenReturn(Optional.empty());

        UnauthorizedException thrown = assertThrows(UnauthorizedException.class,
                () -> authService.refresh(REFRESH_TOKEN_VALUE));

        assertEquals("Refresh token is invalid or expired", thrown.getMessage());
        verify(customUserDetailsService, never()).loadUserByUsername(any());
    }

    @Test
    void refresh_shouldThrowUnauthorizedExceptionIfUserNotFound() {
        when(refreshTokenService.findByToken(REFRESH_TOKEN_VALUE)).thenReturn(Optional.of(refreshToken));
        when(customUserDetailsService.loadUserByUsername(USERNAME)).thenThrow(new BadCredentialsException("User not found"));

        assertThrows(BadCredentialsException.class, () -> authService.refresh(REFRESH_TOKEN_VALUE));

        verify(jwtService, never()).generateToken(any(), any());
    }
}