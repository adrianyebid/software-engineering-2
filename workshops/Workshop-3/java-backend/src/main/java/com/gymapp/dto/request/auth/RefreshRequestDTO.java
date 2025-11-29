package com.gymapp.dto.request.auth;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequestDTO(
        @NotBlank(message = "Refresh token is required")
        String refreshToken
) {}

