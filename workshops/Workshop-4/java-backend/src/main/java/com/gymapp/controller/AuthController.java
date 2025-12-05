package com.gymapp.controller;

import com.gymapp.dto.request.auth.ChangeLoginRequestDTO;
import com.gymapp.dto.request.auth.LoginRequestDTO;
import com.gymapp.dto.request.auth.RefreshRequestDTO;
import com.gymapp.dto.response.auth.AuthResponseDTO;
import com.gymapp.dto.response.auth.RefreshResponseDTO;
import com.gymapp.openapi.annotation.operation.ChangePasswordOperation;
import com.gymapp.openapi.annotation.operation.LoginOperation;
import com.gymapp.service.core.AuthService;
import com.gymapp.service.core.UserPasswordManager;
import com.gymapp.service.security.RefreshTokenService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Operations related to Authentication and Authorities")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final UserPasswordManager userPasswordManager;

    @LoginOperation(summary = "User login", description = "Authenticates a user with username and password")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        AuthResponseDTO response = authService.login(dto.username(), dto.password());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponseDTO> refresh(@RequestBody @Valid RefreshRequestDTO dto) {
        RefreshResponseDTO response = authService.refresh(dto.refreshToken());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid RefreshRequestDTO dto) {
        refreshTokenService.revokeToken(dto.refreshToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @SecurityRequirement(name = "bearerAuth")
    @ChangePasswordOperation(summary = "Change password", description = "Changes the password for an authenticated user")
    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangeLoginRequestDTO dto) {
        userPasswordManager.changePassword(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}