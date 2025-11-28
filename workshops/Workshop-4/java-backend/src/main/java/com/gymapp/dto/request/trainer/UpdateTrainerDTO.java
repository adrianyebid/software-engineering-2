package com.gymapp.dto.request.trainer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateTrainerDTO(
        @NotBlank String username,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotNull Boolean isActive
) {}