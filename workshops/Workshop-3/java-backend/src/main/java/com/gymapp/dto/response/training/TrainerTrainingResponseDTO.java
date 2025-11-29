package com.gymapp.dto.response.training;

import java.time.LocalDate;

public record TrainerTrainingResponseDTO(
        String trainingName,
        LocalDate trainingDate,
        String trainingType,
        Integer trainingDuration,
        String traineeName
) {}
