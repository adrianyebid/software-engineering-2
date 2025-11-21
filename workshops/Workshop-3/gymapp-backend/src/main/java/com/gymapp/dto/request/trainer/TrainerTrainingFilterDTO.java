package com.gymapp.dto.request.trainer;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record TrainerTrainingFilterDTO(
        @NotBlank String username,
        LocalDate periodFrom,
        LocalDate periodTo,
        String traineeName
) {}