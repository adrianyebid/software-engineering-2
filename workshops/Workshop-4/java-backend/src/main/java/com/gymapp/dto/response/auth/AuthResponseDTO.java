package com.gymapp.dto.response.auth;

public record AuthResponseDTO(
        String accessToken,
        String refreshToken,
        String tokenType,
        String role
) {}
