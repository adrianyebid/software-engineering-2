package com.gymapp.dto.request.trainer;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerTrainingFilterDTO {

    @NotBlank(message = "Username is required")
    private String username;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate periodFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate periodTo;

    private String traineeUsername;
}