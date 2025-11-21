package com.gymapp.service.trainee;

import com.gymapp.dto.request.common.ChangeStatusDTO;
import com.gymapp.dto.request.trainee.*;
import com.gymapp.dto.response.trainee.*;
import com.gymapp.dto.response.training.TraineeTrainingResponseDTO;
import com.gymapp.mapper.TraineeMapper;
import com.gymapp.model.Trainee;
import com.gymapp.model.Trainer;
import com.gymapp.repository.TraineeRepository;
import com.gymapp.repository.TrainerRepository;
import com.gymapp.service.trainee.factory.TraineeFactory;
import com.gymapp.service.trainee.query.TraineeQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeServiceImplTest {

    private TraineeRepository traineeRepo;
    private TrainerRepository trainerRepo;
    private TraineeMapper mapper;
    private TraineeFactory factory;
    private TraineeQueryService queryService;
    private TraineeServiceImpl service;

    @BeforeEach
    void setUp() {
        traineeRepo = mock(TraineeRepository.class);
        trainerRepo = mock(TrainerRepository.class);
        mapper = mock(TraineeMapper.class);
        factory = mock(TraineeFactory.class);
        queryService = mock(TraineeQueryService.class);
        service = new TraineeServiceImpl(traineeRepo, trainerRepo, mapper, factory, queryService);
    }

    @Test
    void shouldRegisterTrainee() {
        CreateTraineeDTO dto = new CreateTraineeDTO("Sofia", "Ramirez", LocalDate.of(2000, 1, 1), "Calle 123");
        Trainee trainee = Trainee.builder().username("sofia.ramirez").password("rawpass").build();
        TraineeCredentialsResponseDTO response = new TraineeCredentialsResponseDTO("sofia.ramirez", "rawpass");

        when(factory.create(dto)).thenReturn(trainee);
        when(mapper.toCredentialsDTO(trainee)).thenReturn(response);

        TraineeCredentialsResponseDTO result = service.register(dto);

        assertEquals("sofia.ramirez", result.username());
        assertEquals("rawpass", result.password());
    }

    @Test
    void shouldReturnProfile() {
        Trainee trainee = Trainee.builder().username("sofia.ramirez").build();
        TraineeProfileResponseDTO response = new TraineeProfileResponseDTO("Sofia", "Ramirez", null, "Calle 123", true, List.of());

        when(traineeRepo.findByUsername("sofia.ramirez")).thenReturn(Optional.of(trainee));
        when(mapper.toProfileDTO(trainee)).thenReturn(response);

        TraineeProfileResponseDTO result = service.getProfile("sofia.ramirez");

        assertEquals("Sofia", result.firstName());
    }

    @Test
    void shouldUpdateProfile() {
        UpdateTraineeDTO dto = new UpdateTraineeDTO("sofia.ramirez", "New", "Name", LocalDate.of(2000, 1, 1), "New Address", true);
        Trainee trainee = Trainee.builder().username("sofia.ramirez").build();
        TraineeResponseDTO response = new TraineeResponseDTO("sofia.ramirez", "New", "Name", dto.dateOfBirth(), dto.address(), true, List.of());

        when(traineeRepo.findByUsername("sofia.ramirez")).thenReturn(Optional.of(trainee));
        doNothing().when(factory).update(trainee, dto);
        when(mapper.toResponseDTO(trainee)).thenReturn(response);

        TraineeResponseDTO result = service.updateProfile(dto);

        assertEquals("New", result.firstName());
    }

    @Test
    void shouldChangeStatusToActive() {
        ChangeStatusDTO dto = new ChangeStatusDTO("sofia.ramirez", true);
        Trainee trainee = spy(Trainee.builder().username("sofia.ramirez").isActive(false).build());

        when(traineeRepo.findByUsername("sofia.ramirez")).thenReturn(Optional.of(trainee));

        service.changeStatus(dto);

        verify(trainee).activate();
        verify(traineeRepo).save(trainee);
    }

    @Test
    void shouldChangeStatusToInactive() {
        ChangeStatusDTO dto = new ChangeStatusDTO("sofia.ramirez", false);
        Trainee trainee = spy(Trainee.builder().username("sofia.ramirez").isActive(true).build());

        when(traineeRepo.findByUsername("sofia.ramirez")).thenReturn(Optional.of(trainee));

        service.changeStatus(dto);

        verify(trainee).deactivate();
        verify(traineeRepo).save(trainee);
    }

    @Test
    void shouldDeleteTrainee() {
        Trainee trainee = Trainee.builder().username("sofia.ramirez").build();

        when(traineeRepo.findByUsername("sofia.ramirez")).thenReturn(Optional.of(trainee));

        service.delete("sofia.ramirez");

        verify(factory).delete(trainee);
    }

    @Test
    void shouldAssignTrainers() {
        AssignTrainersDTO dto = new AssignTrainersDTO("sofia.ramirez", List.of("ana.lopez", "miguel.torres"));
        Trainee trainee = Trainee.builder().username("sofia.ramirez").build();
        Trainer trainer1 = Trainer.builder().username("ana.lopez").build();
        Trainer trainer2 = Trainer.builder().username("miguel.torres").build();
        List<Trainer> trainers = List.of(trainer1, trainer2);
        List<TrainerSummaryResponseDTO> response = List.of(
                new TrainerSummaryResponseDTO("ana.lopez", "Ana", "Lopez", "Yoga"),
                new TrainerSummaryResponseDTO("miguel.torres", "Miguel", "Torres", "Pilates")
        );

        when(traineeRepo.findByUsername("sofia.ramirez")).thenReturn(Optional.of(trainee));
        when(trainerRepo.findAllByUsernameIn(dto.trainerUsernames())).thenReturn(trainers);
        when(mapper.toTrainerSummaryList(trainers)).thenReturn(response);

        List<TrainerSummaryResponseDTO> result = service.assignTrainers(dto);

        assertEquals(2, result.size());
        verify(traineeRepo).save(trainee);
    }

    @Test
    void shouldReturnActiveUnassignedTrainers() {
        Trainee trainee = Trainee.builder().username("sofia.ramirez").build();
        Trainer trainer = Trainer.builder().username("ana.lopez").build();
        List<Trainer> trainers = List.of(trainer);
        List<TrainerSummaryResponseDTO> response = List.of(
                new TrainerSummaryResponseDTO("ana.lopez", "Ana", "Lopez", "Yoga")
        );

        when(traineeRepo.findByUsername("sofia.ramirez")).thenReturn(Optional.of(trainee));
        when(trainerRepo.findActiveTrainersNotAssignedToTrainee(trainee)).thenReturn(trainers);
        when(mapper.toTrainerSummaryList(trainers)).thenReturn(response);

        List<TrainerSummaryResponseDTO> result = service.getActiveUnassignedTrainers("sofia.ramirez");

        assertEquals(1, result.size());
        assertEquals("ana.lopez", result.getFirst().username());
    }

    @Test
    void shouldReturnTrainingsByCriteria() {
        TraineeTrainingFilterDTO filter = new TraineeTrainingFilterDTO("sofia.ramirez", null, null, null, null);
        List<TraineeTrainingResponseDTO> expected = List.of(
                new TraineeTrainingResponseDTO("Yoga", LocalDate.of(2025, 10, 10), "Yoga", 60, "Miguel Torres")
        );

        when(queryService.findTrainingsByCriteria("sofia.ramirez", null, null, null, null)).thenReturn(expected);

        List<TraineeTrainingResponseDTO> result = service.getTrainingsByCriteria(filter);

        assertEquals(1, result.size());
        assertEquals("Yoga", result.getFirst().trainingName());
    }
}