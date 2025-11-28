package com.gymapp.controller;

import com.gymapp.dto.response.trainingtype.TrainingTypeResponseDTO;
import com.gymapp.service.trainingtype.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingTypeControllerTest {

    private TrainingTypeService trainingTypeService;
    private TrainingTypeController controller;

    @BeforeEach
    void setUp() {
        trainingTypeService = mock(TrainingTypeService.class);
        controller = new TrainingTypeController(trainingTypeService);
    }

    @Test
    void shouldReturnAllTrainingTypes() {
        TrainingTypeResponseDTO dto1 = new TrainingTypeResponseDTO(1L, "Yoga");
        TrainingTypeResponseDTO dto2 = new TrainingTypeResponseDTO(2L, "Pilates");
        List<TrainingTypeResponseDTO> expected = List.of(dto1, dto2);

        when(trainingTypeService.getAllTrainingTypes()).thenReturn(expected);

        var response = controller.getAllTrainingTypes();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Yoga", response.getBody().getFirst().name());
        verify(trainingTypeService).getAllTrainingTypes();
    }
}
