package com.gymapp.mapper;

import com.gymapp.dto.request.training.CreateTrainingDTO;
import com.gymapp.dto.response.training.TraineeTrainingResponseDTO;
import com.gymapp.dto.response.training.TrainerTrainingResponseDTO;
import com.gymapp.model.Training;
import com.gymapp.model.Trainer;
import com.gymapp.model.Trainee;
import com.gymapp.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TrainingMapperTest {

    private TrainingMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TrainingMapper();
    }

    @Test
    void shouldMapCreateTrainingDTOToEntity() {
        CreateTrainingDTO dto = new CreateTrainingDTO(
                "sofia.ramirez",       // traineeUsername
                "trainer.juan",        // trainerUsername
                "Morning Yoga",        // trainingName
                LocalDate.of(2025, 11, 8),
                60
        );

        Trainee trainee = Trainee.builder().username("sofia.ramirez").build();
        Trainer trainer = Trainer.builder().username("trainer.juan").build();
        TrainingType type = TrainingType.builder().trainingTypeName("Yoga").build();

        Training training = mapper.toEntity(dto, trainee, trainer, type);

        assertEquals("Morning Yoga", training.getTrainingName());
        assertEquals(LocalDate.of(2025, 11, 8), training.getTrainingDate());
        assertEquals(60, training.getTrainingDuration());
        assertEquals("Yoga", training.getTrainingType().getTrainingTypeName());
        assertEquals("sofia.ramirez", training.getTrainee().getUsername());
        assertEquals("trainer.juan", training.getTrainer().getUsername());
    }

    @Test
    void shouldMapTrainingToTraineeResponseDTO() {
        Trainer trainer = Trainer.builder()
                .firstName("Juan")
                .lastName("Perez")
                .build();

        TrainingType type = TrainingType.builder().trainingTypeName("Pilates").build();

        Training training = Training.builder()
                .trainingName("Pilates Session")
                .trainingDate(LocalDate.of(2025, 11, 9))
                .trainingDuration(45)
                .trainingType(type)
                .trainer(trainer)
                .build();

        TraineeTrainingResponseDTO dto = mapper.toTraineeResponseDTO(training);

        assertEquals("Pilates Session", dto.trainingName());
        assertEquals(LocalDate.of(2025, 11, 9), dto.trainingDate());
        assertEquals("Pilates", dto.trainingType());
        assertEquals(45, dto.trainingDuration());
        assertEquals("Juan Perez", dto.trainerName());
    }

    @Test
    void shouldMapTrainingToTrainerResponseDTO() {
        Trainee trainee = Trainee.builder()
                .firstName("Sofia")
                .lastName("Ramirez")
                .build();

        TrainingType type = TrainingType.builder().trainingTypeName("Crossfit").build();

        Training training = Training.builder()
                .trainingName("Crossfit Blast")
                .trainingDate(LocalDate.of(2025, 11, 10))
                .trainingDuration(30)
                .trainingType(type)
                .trainee(trainee)
                .build();

        TrainerTrainingResponseDTO dto = mapper.toTrainerResponseDTO(training);

        assertEquals("Crossfit Blast", dto.trainingName());
        assertEquals(LocalDate.of(2025, 11, 10), dto.trainingDate());
        assertEquals("Crossfit", dto.trainingType());
        assertEquals(30, dto.trainingDuration());
        assertEquals("Sofia Ramirez", dto.traineeName());
    }
}