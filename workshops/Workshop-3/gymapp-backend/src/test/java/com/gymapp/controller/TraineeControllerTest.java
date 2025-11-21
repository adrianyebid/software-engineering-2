package com.gymapp.controller;

import com.gymapp.dto.request.common.ChangeStatusDTO;
import com.gymapp.dto.request.trainee.*;
import com.gymapp.dto.response.trainee.*;
import com.gymapp.dto.response.training.TraineeTrainingResponseDTO;
import com.gymapp.service.trainee.TraineeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeControllerTest {

    private TraineeService traineeService;
    private TraineeController controller;

    @BeforeEach
    void setUp() {
        traineeService = mock(TraineeService.class);
        controller = new TraineeController(traineeService);
    }

    @Test
    void shouldRegisterTrainee() {
        CreateTraineeDTO dto = new CreateTraineeDTO("Sofia", "Ramirez", LocalDate.of(2000, 1, 1), "Calle 123");
        TraineeCredentialsResponseDTO response = new TraineeCredentialsResponseDTO("sofia.ramirez", "rawpass");

        when(traineeService.register(dto)).thenReturn(response);

        var result = controller.register(dto);

        assertEquals(201, result.getStatusCode().value());
        assertEquals("sofia.ramirez", result.getBody().username());
    }

    @Test
    void shouldReturnProfile() {
        TraineeProfileResponseDTO response = new TraineeProfileResponseDTO("Sofia", "Ramirez", null, "Calle 123", true, List.of());

        when(traineeService.getProfile("sofia.ramirez")).thenReturn(response);

        var result = controller.getProfile("sofia.ramirez");

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals("Sofia", result.getBody().firstName());
    }

    @Test
    void shouldUpdateProfile() {
        UpdateTraineeDTO dto = new UpdateTraineeDTO("sofia.ramirez", "New", "Name", LocalDate.of(2000, 1, 1), "New Address", true);
        TraineeResponseDTO response = new TraineeResponseDTO("sofia.ramirez", "New", "Name", dto.dateOfBirth(), dto.address(), true, List.of());

        when(traineeService.updateProfile(dto)).thenReturn(response);

        var result = controller.updateProfile(dto);

        assertEquals(200, result.getStatusCode().value());
        assertEquals("New", result.getBody().firstName());
    }

    @Test
    void shouldDeleteTrainee() {
        doNothing().when(traineeService).delete("sofia.ramirez");

        var result = controller.delete("sofia.ramirez");

        assertEquals(204, result.getStatusCode().value());
        verify(traineeService).delete("sofia.ramirez");
    }

    @Test
    void shouldReturnUnassignedTrainers() {
        TrainerSummaryResponseDTO dto = new TrainerSummaryResponseDTO("ana.lopez", "Ana", "Lopez", "Yoga");

        when(traineeService.getActiveUnassignedTrainers("sofia.ramirez")).thenReturn(List.of(dto));

        var result = controller.getActiveUnassignedTrainers("sofia.ramirez");

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals("ana.lopez", result.getBody().getFirst().username());
    }

    @Test
    void shouldAssignTrainers() {
        AssignTrainersDTO dto = new AssignTrainersDTO("sofia.ramirez", List.of("ana.lopez"));
        TrainerSummaryResponseDTO response = new TrainerSummaryResponseDTO("ana.lopez", "Ana", "Lopez", "Yoga");

        when(traineeService.assignTrainers(dto)).thenReturn(List.of(response));

        var result = controller.assignTrainers(dto);

        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals("ana.lopez", result.getBody().getFirst().username());
    }

//    @Test
//    void shouldReturnTrainingsByCriteria() {
//        TraineeTrainingFilterDTO dto = new TraineeTrainingFilterDTO("sofia.ramirez", null, null, null, null);
//        TraineeTrainingResponseDTO response = new TraineeTrainingResponseDTO("Yoga", LocalDate.of(2025, 10, 10), "Yoga", 60, "Miguel Torres");
//
//        when(traineeService.getTrainingsByCriteria(dto)).thenReturn(List.of(response));
//
//        var result = controller.getTrainingsByCriteria(dto);
//
//        assertEquals(200, result.getStatusCode().value());
//        assertNotNull(result.getBody());
//        assertEquals("Yoga", result.getBody().getFirst().trainingName());
//    }

    @Test
    void shouldChangeStatus() {
        ChangeStatusDTO dto = new ChangeStatusDTO("sofia.ramirez", true);

        doNothing().when(traineeService).changeStatus(dto);

        var result = controller.changeStatus(dto);

        assertEquals(204, result.getStatusCode().value());
        verify(traineeService).changeStatus(dto);
    }
}
