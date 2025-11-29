package com.gymapp.controller;

import com.gymapp.dto.request.auth.ChangeLoginRequestDTO;
import com.gymapp.dto.request.auth.LoginRequestDTO;
import com.gymapp.dto.request.auth.RefreshRequestDTO;
import com.gymapp.dto.response.auth.AuthErrorResponseDTO;
import com.gymapp.dto.response.auth.AuthResponseDTO;
import com.gymapp.dto.response.auth.RefreshResponseDTO;
import com.gymapp.model.RefreshToken;
import com.gymapp.openapi.annotation.operation.ChangePasswordOperation;
import com.gymapp.openapi.annotation.operation.LoginOperation;
import com.gymapp.service.core.UserPasswordManager;
import com.gymapp.service.security.BruteForceProtectionService;
import com.gymapp.service.security.CustomUserDetailsService;
import com.gymapp.service.security.JwtService;
import com.gymapp.service.security.RefreshTokenService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String TOKEN_TYPE = "tokenType";
    public static final String BEARER = "Bearer";

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserPasswordManager userPasswordManager;
    private final BruteForceProtectionService bruteForceProtectionService;



    public AuthController(AuthenticationManager authManager,
                          JwtService jwtService,
                          RefreshTokenService refreshTokenService,
                          CustomUserDetailsService customUserDetailsService,
                          UserPasswordManager userPasswordManager,
                          BruteForceProtectionService bruteForceProtectionService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.customUserDetailsService = customUserDetailsService;
        this.userPasswordManager = userPasswordManager;
        this.bruteForceProtectionService = bruteForceProtectionService;

    }

    @LoginOperation(summary = "User login", description = "Authenticates a user with username and password")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO dto) {
        String username = dto.username();

        if (bruteForceProtectionService.isBlocked(username)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(new AuthErrorResponseDTO("User temporarily locked due to too many failed attempts"));
        }

        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, dto.password()));

            bruteForceProtectionService.loginSucceeded(username);

            String accessToken = jwtService.generateToken(auth.getName(), auth.getAuthorities());
            RefreshToken refreshToken = refreshTokenService.createToken(auth.getName());

            // Extraer rol principal
            String role = auth.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("UNKNOWN");

            AuthResponseDTO response = new AuthResponseDTO(
                    accessToken,
                    refreshToken.getToken(),
                    BEARER,
                    role
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            bruteForceProtectionService.loginFailed(username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthErrorResponseDTO("Invalid credentials"));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponseDTO> refresh(@RequestBody @Valid RefreshRequestDTO dto) {
        return refreshTokenService.findByToken(dto.refreshToken())
                .map(rt -> {
                    String username = rt.getUsername();

                    // Cargar roles reales del usuario
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                    // Generar nuevo access token con roles correctos
                    String newAccessToken = jwtService.generateToken(
                            userDetails.getUsername(),
                            userDetails.getAuthorities()
                    );

                    RefreshResponseDTO response = new RefreshResponseDTO(
                            newAccessToken,
                            BEARER
                    );

                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
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