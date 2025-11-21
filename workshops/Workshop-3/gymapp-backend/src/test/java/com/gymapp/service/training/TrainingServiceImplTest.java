package com.gymapp.service.training;

import com.gymapp.dto.request.training.CreateTrainingDTO;
import com.gymapp.service.training.factory.TrainingFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

class TrainingServiceImplTest {

    private TrainingFactory factory;
    private TrainingServiceImpl service;

    @BeforeEach
    void setUp() {
        factory = mock(TrainingFactory.class);
        service = new TrainingServiceImpl(factory);
    }

    @Test
    void shouldDelegateTrainingCreationToFactory() {
        CreateTrainingDTO dto = new CreateTrainingDTO(
                "sofia.ramirez",
                "ana.lopez",
                "Clase avanzada",
                LocalDate.of(2025, 10, 12),
                45
        );

        service.addTraining(dto);

        verify(factory).create(dto);
    }
}