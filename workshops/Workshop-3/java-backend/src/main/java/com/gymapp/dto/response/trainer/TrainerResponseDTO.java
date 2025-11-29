package com.gymapp.dto.response.trainer;

import java.util.List;

public record TrainerResponseDTO(
        String username,
        String firstName,
        String lastName,
        String specialization,
        boolean isActive,
        List<TraineeSummaryResponseDTO> trainees
) {}