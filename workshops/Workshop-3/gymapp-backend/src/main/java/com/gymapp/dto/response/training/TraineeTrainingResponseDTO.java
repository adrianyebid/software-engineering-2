package com.gymapp.dto.response.training;

import java.time.LocalDate;

public record TraineeTrainingResponseDTO(
        String trainingName,
        LocalDate trainingDate,
        String trainingType,
        Integer trainingDuration,
        String trainerName
) {}
