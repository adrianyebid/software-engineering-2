package com.gymapp.dto.response.trainee;

public record TrainerSummaryResponseDTO(
        String username,
        String firstName,
        String lastName,
        String specialization
) {}