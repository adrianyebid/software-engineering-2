package com.gymapp.facade;

import com.gymapp.dto.request.common.ChangeStatusDTO;
import com.gymapp.dto.request.trainee.*;
import com.gymapp.dto.request.trainer.*;
import com.gymapp.dto.request.training.CreateTrainingDTO;
import com.gymapp.dto.response.trainee.*;
import com.gymapp.dto.response.trainer.*;
import com.gymapp.dto.response.training.*;
import com.gymapp.dto.response.trainingtype.TrainingTypeResponseDTO;
import com.gymapp.service.trainee.TraineeService;
import com.gymapp.service.trainer.TrainerService;
import com.gymapp.service.training.TrainingService;
import com.gymapp.service.trainingtype.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GymAppFacadeTest {

    private TraineeService traineeService;
    private TrainerService trainerService;
    private TrainingService trainingService;
    private TrainingTypeService trainingTypeService;
    private GymAppFacade facade;

    @BeforeEach
    void setUp() {
        traineeService = mock(TraineeService.class);
        trainerService = mock(TrainerService.class);
        trainingService = mock(TrainingService.class);
        trainingTypeService = mock(TrainingTypeService.class);
        facade = new GymAppFacade(traineeService, trainerService, trainingService, trainingTypeService);
    }

    // ───── Trainee ─────

    @Test
    void shouldRegisterTrainee() {
        CreateTraineeDTO dto = new CreateTraineeDTO("Sofia", "Ramirez", LocalDate.of(1995, 5, 15), "Calle 123");
        TraineeCredentialsResponseDTO response = new TraineeCredentialsResponseDTO("sofia.ramirez", "rawpass");

        when(traineeService.register(dto)).thenReturn(response);

        assertEquals(response, facade.registerTrainee(dto));
        verify(traineeService).register(dto);
    }

    @Test
    void shouldGetTraineeProfile() {
        TraineeProfileResponseDTO response = new TraineeProfileResponseDTO("Sofia", "Ramirez", null, "Calle 123", true, List.of());

        when(traineeService.getProfile("sofia.ramirez")).thenReturn(response);

        assertEquals(response, facade.getTraineeProfile("sofia.ramirez"));
        verify(traineeService).getProfile("sofia.ramirez");
    }

    @Test
    void shouldUpdateTraineeProfile() {
        UpdateTraineeDTO dto = new UpdateTraineeDTO("sofia.ramirez", "Sofia", "Ramirez", LocalDate.of(1995, 5, 15), "Nueva dirección", true);
        TraineeResponseDTO response = new TraineeResponseDTO("sofia.ramirez", "Sofia", "Ramirez", dto.dateOfBirth(), dto.address(), true, List.of());

        when(traineeService.updateProfile(dto)).thenReturn(response);

        assertEquals(response, facade.updateTraineeProfile(dto));
        verify(traineeService).updateProfile(dto);
    }

    @Test
    void shouldChangeTraineeStatus() {
        ChangeStatusDTO dto = new ChangeStatusDTO("sofia.ramirez", false);

        facade.changeTraineeStatus(dto);

        verify(traineeService).changeStatus(dto);
    }

    @Test
    void shouldDeleteTrainee() {
        facade.deleteTrainee("sofia.ramirez");

        verify(traineeService).delete("sofia.ramirez");
    }

    @Test
    void shouldAssignTrainersToTrainee() {
        AssignTrainersDTO dto = new AssignTrainersDTO("sofia.ramirez", List.of("ana.lopez"));
        TrainerSummaryResponseDTO trainer = new TrainerSummaryResponseDTO("ana.lopez", "Ana", "Lopez", "Yoga");

        when(traineeService.assignTrainers(dto)).thenReturn(List.of(trainer));

        assertEquals(List.of(trainer), facade.assignTrainersToTrainee(dto));
        verify(traineeService).assignTrainers(dto);
    }

    @Test
    void shouldGetUnassignedTrainersForTrainee() {
        TrainerSummaryResponseDTO trainer = new TrainerSummaryResponseDTO("ana.lopez", "Ana", "Lopez", "Yoga");

        when(traineeService.getActiveUnassignedTrainers("sofia.ramirez")).thenReturn(List.of(trainer));

        assertEquals(List.of(trainer), facade.getUnassignedTrainersForTrainee("sofia.ramirez"));
        verify(traineeService).getActiveUnassignedTrainers("sofia.ramirez");
    }

    @Test
    void shouldGetTraineeTrainings() {
        TraineeTrainingFilterDTO filter = new TraineeTrainingFilterDTO("sofia.ramirez", null, null, null, null);
        TraineeTrainingResponseDTO training = new TraineeTrainingResponseDTO("Yoga", LocalDate.of(2025, 10, 12), "Yoga", 45, "Ana Lopez");

        when(traineeService.getTrainingsByCriteria(filter)).thenReturn(List.of(training));

        assertEquals(List.of(training), facade.getTraineeTrainings(filter));
        verify(traineeService).getTrainingsByCriteria(filter);
    }

    // ───── Trainer ─────

    @Test
    void shouldRegisterTrainer() {
        CreateTrainerDTO dto = new CreateTrainerDTO("Ana", "Lopez", "Yoga");
        TrainerCredentialsResponseDTO response = new TrainerCredentialsResponseDTO("ana.lopez", "rawpass");

        when(trainerService.register(dto)).thenReturn(response);

        assertEquals(response, facade.registerTrainer(dto));
        verify(trainerService).register(dto);
    }

    @Test
    void shouldGetTrainerProfile() {
        TrainerProfileResponseDTO response = new TrainerProfileResponseDTO("Ana", "Lopez", "Yoga", true, List.of());

        when(trainerService.getProfile("ana.lopez")).thenReturn(response);

        assertEquals(response, facade.getTrainerProfile("ana.lopez"));
        verify(trainerService).getProfile("ana.lopez");
    }

    @Test
    void shouldUpdateTrainerProfile() {
        UpdateTrainerDTO dto = new UpdateTrainerDTO("ana.lopez", "Ana", "Lopez", true);
        TrainerResponseDTO response = new TrainerResponseDTO("ana.lopez", "Ana", "Lopez", "Yoga", true, List.of());

        when(trainerService.updateProfile(dto)).thenReturn(response);

        assertEquals(response, facade.updateTrainerProfile(dto));
        verify(trainerService).updateProfile(dto);
    }

    @Test
    void shouldChangeTrainerStatus() {
        ChangeStatusDTO dto = new ChangeStatusDTO("ana.lopez", false);

        facade.changeTrainerStatus(dto);

        verify(trainerService).changeStatus(dto);
    }

    @Test
    void shouldDeleteTrainer() {
        facade.deleteTrainer("ana.lopez");

        verify(trainerService).delete("ana.lopez");
    }

    @Test
    void shouldGetTraineesAssignedToTrainer() {
        TraineeSummaryResponseDTO trainee = new TraineeSummaryResponseDTO("sofia.ramirez", "Sofia", "Ramirez");

        when(trainerService.getAssignedTrainees("ana.lopez")).thenReturn(List.of(trainee));

        assertEquals(List.of(trainee), facade.getTraineesAssignedToTrainer("ana.lopez"));
        verify(trainerService).getAssignedTrainees("ana.lopez");
    }

    @Test
    void shouldGetTrainerTrainings() {
        TrainerTrainingFilterDTO filter = new TrainerTrainingFilterDTO("ana.lopez", null, null, null);
        TrainerTrainingResponseDTO training = new TrainerTrainingResponseDTO("Clase avanzada", LocalDate.of(2025, 10, 12), "Yoga", 45, "Sofia Ramirez");

        when(trainerService.getTrainingsByCriteria(filter)).thenReturn(List.of(training));

        assertEquals(List.of(training), facade.getTrainerTrainings(filter));
        verify(trainerService).getTrainingsByCriteria(filter);
    }

    // ───── Training ─────

    @Test
    void shouldAddTraining() {
        CreateTrainingDTO dto = new CreateTrainingDTO("sofia.ramirez", "ana.lopez", "Clase avanzada", LocalDate.of(2025, 10, 12), 45);

        facade.addTraining(dto);

        verify(trainingService).addTraining(dto);
    }

    // ───── Training Types ─────

    @Test
    void shouldGetAllTrainingTypes() {
        TrainingTypeResponseDTO type = new TrainingTypeResponseDTO(1L, "Yoga");

        when(trainingTypeService.getAllTrainingTypes()).thenReturn(List.of(type));

        assertEquals(List.of(type), facade.getAllTrainingTypes());
        verify(trainingTypeService).getAllTrainingTypes();
    }
}