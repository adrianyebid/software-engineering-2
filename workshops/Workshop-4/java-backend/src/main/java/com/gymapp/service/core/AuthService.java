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
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    public static final String UNKNOWN = "UNKNOWN";
    public static final String TOKEN_TYPE = "Bearer";
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final CustomUserDetailsService customUserDetailsService;
    private final BruteForceProtectionService bruteForceProtectionService;

    public AuthResponseDTO login(String username, String password) {

        if (bruteForceProtectionService.isBlocked(username)) {
            throw new TooManyRequestsException("User temporarily locked due to too many failed attempts");
        }

        try {

            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            bruteForceProtectionService.loginSucceeded(username);

            String accessToken = jwtService.generateToken(auth.getName(), auth.getAuthorities());
            RefreshToken refreshToken = refreshTokenService.createToken(auth.getName());

            String role = auth.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse(UNKNOWN);

            return new AuthResponseDTO(accessToken, refreshToken.getToken(), TOKEN_TYPE, role);

        } catch (AuthenticationException e) {
            bruteForceProtectionService.loginFailed(username);
            throw new BadCredentialsException("Invalid credentials", e);
        }
    }

    public RefreshResponseDTO refresh(String refreshTokenValue) {
        return refreshTokenService.findByToken(refreshTokenValue)
                .map(rt -> {
                    String username = rt.getUsername();

                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                    String newAccessToken = jwtService.generateToken(
                            userDetails.getUsername(),
                            userDetails.getAuthorities()
                    );

                    return new RefreshResponseDTO(newAccessToken, TOKEN_TYPE);
                })
                .orElseThrow(() -> new UnauthorizedException("Refresh token is invalid or expired"));
    }
}