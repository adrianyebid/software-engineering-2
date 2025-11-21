package com.gymapp.dto.request.trainer;

import jakarta.validation.constraints.NotBlank;

public record CreateTrainerDTO(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String specialization
) {}