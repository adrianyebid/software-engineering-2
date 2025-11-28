package com.gymapp.dto.request.trainee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record UpdateTraineeDTO(
        @NotBlank String username,
        @NotBlank String firstName,
        @NotBlank String lastName,
        LocalDate dateOfBirth,
        String address,
        @NotNull Boolean isActive
) {}