package com.gymapp.dto.request.auth;

import jakarta.validation.constraints.NotBlank;

public record ChangeLoginRequestDTO(
        @NotBlank String username,
        @NotBlank String oldPassword,
        @NotBlank String newPassword
) {}