package com.gymapp.dto.response.trainer;

import java.util.List;

public record TrainerProfileResponseDTO(
        String firstName,
        String lastName,
        String specialization,
        boolean isActive,
        List<TraineeSummaryResponseDTO> trainees
) {
}
