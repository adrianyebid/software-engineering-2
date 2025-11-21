package com.gymapp.controller;

import com.gymapp.dto.request.auth.ChangeLoginRequestDTO;
import com.gymapp.dto.request.auth.LoginRequestDTO;
import com.gymapp.openapi.annotation.operation.ChangePasswordOperation;
import com.gymapp.openapi.annotation.operation.LoginOperation;
import com.gymapp.service.core.AuthManager;
import com.gymapp.service.core.UserPasswordManager;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Login and credential management")
public class AuthController {

    private final AuthManager authManager;
    private final UserPasswordManager userPasswordManager;

    public AuthController(AuthManager authManager, UserPasswordManager userPasswordManager) {
        this.authManager = authManager;
        this.userPasswordManager = userPasswordManager;
    }

    @LoginOperation(summary = "User login", description = "Authenticates a user with username and password")
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequestDTO dto) {
        authManager.authenticate(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ChangePasswordOperation(summary = "Change password", description = "Changes the password for an authenticated user")
    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangeLoginRequestDTO dto) {
        userPasswordManager.changePassword(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}