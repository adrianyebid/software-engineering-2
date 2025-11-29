package com.gymapp.dto.response.auth;

public record RefreshResponseDTO(
        String accessToken,
        String tokenType
) {}
