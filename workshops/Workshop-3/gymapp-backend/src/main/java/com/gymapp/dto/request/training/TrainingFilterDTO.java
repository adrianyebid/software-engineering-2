package com.gymapp.dto.request.training;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record TrainingFilterDTO(
        @NotBlank String username,
        LocalDate periodFrom,
        LocalDate periodTo,
        String trainerName,   // usado por trainee
        String traineeName,   // usado por trainer
        String trainingType
) {}