package com.gymapp.controller;

import com.gymapp.dto.request.auth.ChangeLoginRequestDTO;
import com.gymapp.dto.request.auth.LoginRequestDTO;
import com.gymapp.dto.request.auth.RefreshRequestDTO;
import com.gymapp.dto.response.auth.AuthErrorResponseDTO;
import com.gymapp.dto.response.auth.AuthResponseDTO;
import com.gymapp.dto.response.auth.RefreshResponseDTO;
import com.gymapp.model.RefreshToken;
import com.gymapp.service.core.UserPasswordManager;
import com.gymapp.service.security.BruteForceProtectionService;
import com.gymapp.service.security.CustomUserDetailsService;
import com.gymapp.service.security.JwtService;
import com.gymapp.service.security.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private AuthenticationManager authManager;
    private JwtService jwtService;
    private RefreshTokenService refreshTokenService;
    private CustomUserDetailsService customUserDetailsService;
    private UserPasswordManager userPasswordManager;
    private BruteForceProtectionService bruteForceProtectionService;

    private AuthController controller;

    @BeforeEach
    void setUp() {
        authManager = mock(AuthenticationManager.class);
        jwtService = mock(JwtService.class);
        refreshTokenService = mock(RefreshTokenService.class);
        customUserDetailsService = mock(CustomUserDetailsService.class);
        userPasswordManager = mock(UserPasswordManager.class);
        bruteForceProtectionService = mock(BruteForceProtectionService.class);

        controller = new AuthController(
                authManager,
                jwtService,
                refreshTokenService,
                customUserDetailsService,
                userPasswordManager,
                bruteForceProtectionService
        );
    }

    // ============================================================
    // LOGIN
    // ============================================================
    @Test
    void login_shouldAuthenticateSuccessfully() {
        LoginRequestDTO dto = new LoginRequestDTO("john", "1234");

        // ROLE_TRAINER
        GrantedAuthority role = new SimpleGrantedAuthority("ROLE_TRAINER");
        Authentication auth = new UsernamePasswordAuthenticationToken("john", null, List.of(role));

        when(bruteForceProtectionService.isBlocked("john")).thenReturn(false);
        when(authManager.authenticate(any())).thenReturn(auth);
        when(jwtService.generateToken(eq("john"), any())).thenReturn("ACCESS123");

        RefreshToken rt = new RefreshToken();
        rt.setToken("REFRESH123");
        rt.setUsername("john");
        when(refreshTokenService.createToken("john")).thenReturn(rt);

        ResponseEntity<?> response = controller.login(dto);

        assertEquals(200, response.getStatusCode().value());

        AuthResponseDTO body = (AuthResponseDTO) response.getBody();
        assertEquals("ACCESS123", body.accessToken());
        assertEquals("REFRESH123", body.refreshToken());
        assertEquals("ROLE_TRAINER", body.role());

        verify(bruteForceProtectionService).loginSucceeded("john");
        verify(authManager).authenticate(any());
    }

    @Test
    void login_shouldReturn429IfUserBlocked() {
        LoginRequestDTO dto = new LoginRequestDTO("john", "1234");

        when(bruteForceProtectionService.isBlocked("john")).thenReturn(true);

        ResponseEntity<?> response = controller.login(dto);

        assertEquals(429, response.getStatusCode().value());
        assertTrue(response.getBody() instanceof AuthErrorResponseDTO);
    }

    @Test
    void login_shouldReturn401OnInvalidCredentials() {
        LoginRequestDTO dto = new LoginRequestDTO("john", "badpass");

        when(bruteForceProtectionService.isBlocked("john")).thenReturn(false);
        when(authManager.authenticate(any())).thenThrow(new RuntimeException("bad creds"));

        ResponseEntity<?> response = controller.login(dto);

        assertEquals(401, response.getStatusCode().value());
        verify(bruteForceProtectionService).loginFailed("john");
    }

    // ============================================================
    // REFRESH TOKEN
    // ============================================================
    @Test
    void refresh_shouldReturnNewAccessToken() {
        RefreshRequestDTO dto = new RefreshRequestDTO("REFRESH123");

        RefreshToken token = new RefreshToken();
        token.setUsername("john");

        GrantedAuthority role = new SimpleGrantedAuthority("ROLE_TRAINEE");

        when(refreshTokenService.findByToken("REFRESH123")).thenReturn(Optional.of(token));

        when(customUserDetailsService.loadUserByUsername("john"))
                .thenReturn(new User("john", "pass", List.of(role)));

        when(jwtService.generateToken(eq("john"), any())).thenReturn("NEW_ACCESS");

        ResponseEntity<RefreshResponseDTO> response = controller.refresh(dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("NEW_ACCESS", response.getBody().accessToken());
    }

    @Test
    void refresh_shouldReturn401IfTokenNotFound() {
        RefreshRequestDTO dto = new RefreshRequestDTO("INVALID");

        when(refreshTokenService.findByToken("INVALID")).thenReturn(Optional.empty());

        ResponseEntity<RefreshResponseDTO> response = controller.refresh(dto);

        assertEquals(401, response.getStatusCode().value());
    }

    // ============================================================
    // LOGOUT
    // ============================================================
    @Test
    void logout_shouldRevokeToken() {
        RefreshRequestDTO dto = new RefreshRequestDTO("REFRESH123");

        ResponseEntity<Void> response = controller.logout(dto);

        assertEquals(200, response.getStatusCode().value());
        verify(refreshTokenService).revokeToken("REFRESH123");
    }

    // ============================================================
    // CHANGE PASSWORD
    // ============================================================
    @Test
    void changePassword_shouldWork() {
        ChangeLoginRequestDTO dto =
                new ChangeLoginRequestDTO("john", "oldpass", "newpass");

        ResponseEntity<Void> response = controller.changePassword(dto);

        assertEquals(200, response.getStatusCode().value());
        verify(userPasswordManager).changePassword(dto);
    }
}
