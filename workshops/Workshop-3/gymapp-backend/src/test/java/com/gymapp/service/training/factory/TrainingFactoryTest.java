package com.gymapp.service.training.factory;

import com.gymapp.dto.request.training.CreateTrainingDTO;
import com.gymapp.mapper.TrainingMapper;
import com.gymapp.model.*;
import com.gymapp.repository.TraineeRepository;
import com.gymapp.repository.TrainerRepository;
import com.gymapp.repository.TrainingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingFactoryTest {

    private TrainingRepository trainingRepo;
    private TraineeRepository traineeRepo;
    private TrainerRepository trainerRepo;
    private TrainingMapper trainingMapper;
    private TrainingFactory factory;

    @BeforeEach
    void setUp() {
        trainingRepo = mock(TrainingRepository.class);
        traineeRepo = mock(TraineeRepository.class);
        trainerRepo = mock(TrainerRepository.class);
        trainingMapper = mock(TrainingMapper.class);
        factory = new TrainingFactory(trainingRepo, traineeRepo, trainerRepo, trainingMapper);
    }

    @Test
    void shouldCreateTrainingWithResolvedEntities() {
        CreateTrainingDTO dto = new CreateTrainingDTO(
                "sofia.ramirez",
                "ana.lopez",
                "Clase avanzada",
                LocalDate.of(2025, 10, 12),
                45
        );

        Trainee trainee = Trainee.builder().username("sofia.ramirez").build();
        TrainingType type = TrainingType.builder().trainingTypeName("Yoga").build();
        Trainer trainer = Trainer.builder().username("ana.lopez").trainingType(type).build();

        Training training = Training.builder()
                .trainingName("Clase avanzada")
                .trainingDate(dto.trainingDate())
                .trainingDuration(dto.trainingDuration())
                .trainee(trainee)
                .trainer(trainer)
                .trainingType(type)
                .build();

        when(traineeRepo.findByUsername("sofia.ramirez")).thenReturn(Optional.of(trainee));
        when(trainerRepo.findByUsername("ana.lopez")).thenReturn(Optional.of(trainer));
        when(trainingMapper.toEntity(dto, trainee, trainer, type)).thenReturn(training);
        when(trainingRepo.save(training)).thenReturn(training);

        factory.create(dto);

        verify(trainingRepo).save(training);
    }

    @Test
    void shouldThrowIfTraineeNotFound() {
        CreateTrainingDTO dto = new CreateTrainingDTO("unknown.trainee", "ana.lopez", "Clase", LocalDate.now(), 30);
        when(traineeRepo.findByUsername("unknown.trainee")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> factory.create(dto));
        assertEquals("Trainee no encontrado", ex.getMessage());
    }

    @Test
    void shouldThrowIfTrainerNotFound() {
        CreateTrainingDTO dto = new CreateTrainingDTO("sofia.ramirez", "unknown.trainer", "Clase", LocalDate.now(), 30);
        when(traineeRepo.findByUsername("sofia.ramirez")).thenReturn(Optional.of(new Trainee()));
        when(trainerRepo.findByUsername("unknown.trainer")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> factory.create(dto));
        assertEquals("Trainer no encontrado", ex.getMessage());
    }
}