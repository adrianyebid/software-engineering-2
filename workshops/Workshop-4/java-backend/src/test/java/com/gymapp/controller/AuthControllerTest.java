package com.gymapp.controller;

import com.gymapp.dto.request.auth.ChangeLoginRequestDTO;
import com.gymapp.dto.request.auth.LoginRequestDTO;
import com.gymapp.dto.request.auth.RefreshRequestDTO;
import com.gymapp.dto.response.auth.AuthResponseDTO;
import com.gymapp.dto.response.auth.RefreshResponseDTO;
import com.gymapp.service.core.AuthService;
import com.gymapp.service.core.UserPasswordManager;
import com.gymapp.service.security.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("AuthController Unit Tests (Delegation Check)")
class AuthControllerTest {

    private AuthService authService;
    private RefreshTokenService refreshTokenService;
    private UserPasswordManager userPasswordManager;

    private AuthController controller;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        refreshTokenService = mock(RefreshTokenService.class);
        userPasswordManager = mock(UserPasswordManager.class);

        // Inyección del nuevo constructor (implícito por @RequiredArgsConstructor)
        controller = new AuthController(
                authService,
                refreshTokenService,
                userPasswordManager
        );
    }

    @Test
    @DisplayName("LOGIN: Should delegate to AuthService and return 200 OK")
    void login_shouldDelegateAndReturn200() {
        LoginRequestDTO dto = new LoginRequestDTO("john", "1234");
        AuthResponseDTO expectedResponse = new AuthResponseDTO(
                "ACCESS_TOKEN", "REFRESH_TOKEN", "Bearer", "ROLE_TRAINER"
        );
        when(authService.login(dto.username(), dto.password())).thenReturn(expectedResponse);
        ResponseEntity<AuthResponseDTO> response = controller.login(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Debe retornar 200 OK");
        assertEquals(expectedResponse, response.getBody(), "Debe retornar el DTO del servicio");

        verify(authService).login("john", "1234");
    }

    @Test
    @DisplayName("REFRESH: Should delegate to AuthService and return 200 OK")
    void refresh_shouldDelegateAndReturn200() {
        RefreshRequestDTO dto = new RefreshRequestDTO("REFRESH123");
        RefreshResponseDTO expectedResponse = new RefreshResponseDTO("NEW_ACCESS_TOKEN", "Bearer");

        when(authService.refresh(dto.refreshToken())).thenReturn(expectedResponse);

        ResponseEntity<RefreshResponseDTO> response = controller.refresh(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Debe retornar 200 OK");
        assertEquals(expectedResponse, response.getBody(), "Debe retornar el DTO del servicio");

        verify(authService).refresh("REFRESH123");
    }


    @Test
    @DisplayName("LOGOUT: Should delegate to RefreshTokenService and return 200 OK")
    void logout_shouldRevokeToken() {

        RefreshRequestDTO dto = new RefreshRequestDTO("REFRESH123");

        ResponseEntity<Void> response = controller.logout(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Debe retornar 200 OK");

        verify(refreshTokenService).revokeToken("REFRESH123");
    }

    @Test
    @DisplayName("CHANGE PASSWORD: Should delegate to UserPasswordManager and return 200 OK")
    void changePassword_shouldDelegateAndReturn200() {
        ChangeLoginRequestDTO dto = new ChangeLoginRequestDTO("john", "oldpass", "newpass");
        ResponseEntity<Void> response = controller.changePassword(dto);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Debe retornar 200 OK");
        verify(userPasswordManager).changePassword(dto);
    }
}