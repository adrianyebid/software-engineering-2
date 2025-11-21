package com.gymapp.dto.request.trainee;
import java.time.LocalDate;

public record TraineeTrainingFilterDTO(
        String username,
        LocalDate periodFrom,
        LocalDate periodTo,
        String trainerName,
        String trainingType
) {}
