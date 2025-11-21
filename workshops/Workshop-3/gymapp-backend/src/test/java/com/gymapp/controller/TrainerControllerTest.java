package com.gymapp.controller;

import com.gymapp.dto.request.common.ChangeStatusDTO;
import com.gymapp.dto.request.trainer.CreateTrainerDTO;
import com.gymapp.dto.request.trainer.TrainerTrainingFilterDTO;
import com.gymapp.dto.request.trainer.UpdateTrainerDTO;
import com.gymapp.dto.response.trainer.*;
import com.gymapp.dto.response.training.TrainerTrainingResponseDTO;
import com.gymapp.service.trainer.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerControllerTest {

    private TrainerService trainerService;
    private TrainerController controller;

    @BeforeEach
    void setUp() {
        trainerService = mock(TrainerService.class);
        controller = new TrainerController(trainerService);
    }

    @Test
    void shouldRegisterTrainer() {
        CreateTrainerDTO dto = new CreateTrainerDTO("Ana", "Lopez", "Yoga");
        TrainerCredentialsResponseDTO response = new TrainerCredentialsResponseDTO("ana.lopez", "rawpass");

        when(trainerService.register(dto)).thenReturn(response);

        var result = controller.register(dto);

        assertEquals(201, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals("ana.lopez", result.getBody().username());
    }

    @Test
    void shouldReturnTrainerProfile() {
        TrainerProfileResponseDTO response = new TrainerProfileResponseDTO("Ana", "Lopez", "Yoga", true, List.of());

        when(trainerService.getProfile("ana.lopez")).thenReturn(response);

        var result = controller.getProfile("ana.lopez");

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals("Ana", result.getBody().firstName());
    }

    @Test
    void shouldUpdateTrainerProfile() {
        UpdateTrainerDTO dto = new UpdateTrainerDTO("ana.lopez", "Ana", "Lopez", true);
        TrainerResponseDTO response = new TrainerResponseDTO("ana.lopez", "Ana", "Lopez", "Yoga", true, List.of());

        when(trainerService.updateProfile(dto)).thenReturn(response);

        var result = controller.updateProfile(dto);

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals("Ana", result.getBody().firstName());
    }

    @Test
    void shouldChangeTrainerStatus() {
        ChangeStatusDTO dto = new ChangeStatusDTO("ana.lopez", false);

        doNothing().when(trainerService).changeStatus(dto);

        var result = controller.changeStatus(dto);

        assertEquals(204, result.getStatusCode().value());
        verify(trainerService).changeStatus(dto);
    }

    @Test
    void shouldDeleteTrainer() {
        doNothing().when(trainerService).delete("ana.lopez");

        var result = controller.delete("ana.lopez");

        assertEquals(204, result.getStatusCode().value());
        verify(trainerService).delete("ana.lopez");
    }

    @Test
    void shouldReturnAssignedTrainees() {
        TraineeSummaryResponseDTO dto = new TraineeSummaryResponseDTO("sofia.ramirez", "Sofia", "Ramirez");

        when(trainerService.getAssignedTrainees("ana.lopez")).thenReturn(List.of(dto));

        var result = controller.getAssignedTrainees("ana.lopez");

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals("sofia.ramirez", result.getBody().getFirst().username());
    }


}