package com.gymapp.dto.request.trainee;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record CreateTraineeDTO(
        @NotBlank String firstName,
        @NotBlank String lastName,
        LocalDate dateOfBirth,
        String address
) {}
