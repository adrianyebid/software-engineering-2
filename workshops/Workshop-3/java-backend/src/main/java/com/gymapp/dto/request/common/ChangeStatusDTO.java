package com.gymapp.dto.request.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChangeStatusDTO(
        @NotBlank String username,
        @NotNull Boolean isActive
        ) {}
