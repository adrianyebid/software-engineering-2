package com.gymapp.dto.request.trainee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record AssignTrainersDTO(
        @NotBlank String traineeUsername,
        @NotEmpty List<@NotBlank String> trainerUsernames
) {}