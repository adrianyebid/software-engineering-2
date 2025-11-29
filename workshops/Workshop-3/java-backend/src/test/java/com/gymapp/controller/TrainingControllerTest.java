package com.gymapp.controller;

import com.gymapp.dto.request.training.CreateTrainingDTO;
import com.gymapp.service.training.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TrainingControllerTest {

    private TrainingService trainingService;
    private TrainingController controller;

    @BeforeEach
    void setUp() {
        trainingService = mock(TrainingService.class);
        controller = new TrainingController(trainingService);
    }

    @Test
    void shouldAddTrainingSuccessfully() {
        CreateTrainingDTO dto = new CreateTrainingDTO(
                "sofia.ramirez",
                "ana.lopez",
                "Clase avanzada",
                LocalDate.of(2025, 10, 12),
                45
        );

        doNothing().when(trainingService).addTraining(dto);

        var response = controller.addTraining(dto);

        assertEquals(200, response.getStatusCode().value());
        verify(trainingService).addTraining(dto);
    }
}