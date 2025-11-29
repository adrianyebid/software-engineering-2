package com.gymapp.dto.response.trainee;

import java.time.LocalDate;
import java.util.List;

public record TraineeProfileResponseDTO(
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String address,
        boolean isActive,
        List<TrainerSummaryResponseDTO> trainers
) {
}
