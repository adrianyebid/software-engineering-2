package com.gymapp.service.trainer;

import com.gymapp.dto.request.common.ChangeStatusDTO;
import com.gymapp.dto.request.trainer.CreateTrainerDTO;
import com.gymapp.dto.request.trainer.TrainerTrainingFilterDTO;
import com.gymapp.dto.request.trainer.UpdateTrainerDTO;
import com.gymapp.dto.response.trainer.*;
import com.gymapp.dto.response.training.TrainerTrainingResponseDTO;
import com.gymapp.mapper.TrainerMapper;
import com.gymapp.model.Trainer;
import com.gymapp.model.Trainee;
import com.gymapp.model.TrainingType;
import com.gymapp.repository.TrainerRepository;
import com.gymapp.service.trainer.factory.TrainerFactory;
import com.gymapp.service.trainer.query.TrainerQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceImplTest {

    private TrainerFactory factory;
    private TrainerMapper mapper;
    private TrainerQueryService queryService;
    private TrainerRepository trainerRepo;
    private TrainerServiceImpl service;

    @BeforeEach
    void setUp() {
        factory = mock(TrainerFactory.class);
        mapper = mock(TrainerMapper.class);
        queryService = mock(TrainerQueryService.class);
        trainerRepo = mock(TrainerRepository.class);
        service = new TrainerServiceImpl(factory, mapper, queryService, trainerRepo);
    }

    @Test
    void shouldRegisterTrainer() {
        CreateTrainerDTO dto = new CreateTrainerDTO("Ana", "Lopez", "Yoga");
        TrainerCredentialsResponseDTO response = new TrainerCredentialsResponseDTO("ana.lopez", "rawpass");

        when(factory.create(dto)).thenReturn(response);

        TrainerCredentialsResponseDTO result = service.register(dto);

        assertEquals("ana.lopez", result.username());
        assertEquals("rawpass", result.password());
    }

    @Test
    void shouldReturnTrainerProfile() {
        Trainer trainer = Trainer.builder()
                .firstName("Ana").lastName("Lopez").isActive(true)
                .trainingType(TrainingType.builder().trainingTypeName("Yoga").build())
                .trainees(Set.of())
                .build();

        TrainerProfileResponseDTO response = new TrainerProfileResponseDTO("Ana", "Lopez", "Yoga", true, List.of());

        when(trainerRepo.findByUsername("ana.lopez")).thenReturn(Optional.of(trainer));
        when(mapper.toProfileDTO(trainer)).thenReturn(response);

        TrainerProfileResponseDTO result = service.getProfile("ana.lopez");

        assertEquals("Ana", result.firstName());
        assertEquals("Yoga", result.specialization());
    }

    @Test
    void shouldUpdateTrainerProfile() {
        UpdateTrainerDTO dto = new UpdateTrainerDTO("ana.lopez", "Ana", "Lopez", true);
        Trainer trainer = Trainer.builder().username("ana.lopez").build();
        TrainerResponseDTO response = new TrainerResponseDTO("ana.lopez", "Ana", "Lopez", "Yoga", true, List.of());

        when(trainerRepo.findByUsername("ana.lopez")).thenReturn(Optional.of(trainer));
        doNothing().when(factory).update(trainer, dto);
        when(mapper.toResponseDTO(trainer)).thenReturn(response);

        TrainerResponseDTO result = service.updateProfile(dto);

        assertEquals("Ana", result.firstName());
    }

    @Test
    void shouldActivateTrainer() {
        ChangeStatusDTO dto = new ChangeStatusDTO("ana.lopez", true);
        Trainer trainer = spy(Trainer.builder().username("ana.lopez").isActive(false).build());

        when(trainerRepo.findByUsername("ana.lopez")).thenReturn(Optional.of(trainer));

        service.changeStatus(dto);

        verify(trainer).activate();
        verify(trainerRepo).save(trainer);
    }

    @Test
    void shouldDeactivateTrainer() {
        ChangeStatusDTO dto = new ChangeStatusDTO("ana.lopez", false);
        Trainer trainer = spy(Trainer.builder().username("ana.lopez").isActive(true).build());

        when(trainerRepo.findByUsername("ana.lopez")).thenReturn(Optional.of(trainer));

        service.changeStatus(dto);

        verify(trainer).deactivate();
        verify(trainerRepo).save(trainer);
    }

    @Test
    void shouldDeleteTrainer() {
        Trainer trainer = Trainer.builder().username("ana.lopez").build();

        when(trainerRepo.findByUsername("ana.lopez")).thenReturn(Optional.of(trainer));

        service.delete("ana.lopez");

        verify(factory).delete(trainer);
    }

    @Test
    void shouldReturnAssignedTrainees() {
        Trainee trainee = Trainee.builder().username("sofia.ramirez").firstName("Sofia").lastName("Ramirez").build();
        Trainer trainer = Trainer.builder().username("ana.lopez").trainees(Set.of(trainee)).build();
        TraineeSummaryResponseDTO dto = new TraineeSummaryResponseDTO("sofia.ramirez", "Sofia", "Ramirez");

        when(trainerRepo.findByUsername("ana.lopez")).thenReturn(Optional.of(trainer));
        when(mapper.toTraineeSummaryList(List.of(trainee))).thenReturn(List.of(dto));

        List<TraineeSummaryResponseDTO> result = service.getAssignedTrainees("ana.lopez");

        assertEquals(1, result.size());
        assertEquals("sofia.ramirez", result.getFirst().username());
    }

    @Test
    void shouldReturnTrainerTrainingsByCriteria() {
        TrainerTrainingFilterDTO filter = new TrainerTrainingFilterDTO("ana.lopez", null, null, null);
        TrainerTrainingResponseDTO dto = new TrainerTrainingResponseDTO("Clase avanzada", LocalDate.of(2025, 10, 12), "Yoga", 45, "Sofia Ramirez");

        when(queryService.findTrainingsByCriteria("ana.lopez", null, null, null)).thenReturn(List.of(dto));

        List<TrainerTrainingResponseDTO> result = service.getTrainingsByCriteria(filter);

        assertEquals(1, result.size());
        assertEquals("Clase avanzada", result.getFirst().trainingName());
    }
}