package com.gymapp.mapper;

import com.gymapp.dto.request.trainer.CreateTrainerDTO;
import com.gymapp.dto.request.trainer.UpdateTrainerDTO;
import com.gymapp.dto.response.trainer.TrainerCredentialsResponseDTO;
import com.gymapp.dto.response.trainer.TrainerProfileResponseDTO;
import com.gymapp.dto.response.trainer.TrainerResponseDTO;
import com.gymapp.dto.response.trainer.TraineeSummaryResponseDTO;
import com.gymapp.model.Trainer;
import com.gymapp.model.Trainee;
import com.gymapp.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TrainerMapperTest {

    private TrainerMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TrainerMapper();
    }

    @Test
    void shouldMapCreateTrainerDTOToEntity() {
        CreateTrainerDTO dto = new CreateTrainerDTO("Juan", "Perez", "Yoga");
        TrainingType type = TrainingType.builder().trainingTypeName("Yoga").build();

        Trainer trainer = mapper.toEntity(dto, type);

        assertEquals("Juan", trainer.getFirstName());
        assertEquals("Perez", trainer.getLastName());
        assertEquals("Yoga", trainer.getTrainingType().getTrainingTypeName());
    }

    @Test
    void shouldUpdateTrainerEntityFromUpdateDTO() {
        Trainer trainer = Trainer.builder()
                .firstName("Old")
                .lastName("Name")
                .isActive(false)
                .build();

        UpdateTrainerDTO dto = new UpdateTrainerDTO("trainer.juan", "New", "Name", true);

        mapper.updateEntity(trainer, dto);

        assertEquals("New", trainer.getFirstName());
        assertEquals("Name", trainer.getLastName());
        assertTrue(trainer.getIsActive());
    }

    @Test
    void shouldMapToCredentialsDTO() {
        Trainer trainer = Trainer.builder()
                .username("trainer.juan")
                .password("encodedpass")
                .build();

        TrainerCredentialsResponseDTO dto = mapper.toCredentialsDTO(trainer);

        assertEquals("trainer.juan", dto.username());
        assertEquals("encodedpass", dto.password());
    }

    @Test
    void shouldMapToProfileDTOWithTraineeSummaries() {
        Trainee trainee = Trainee.builder()
                .username("sofia.ramirez")
                .firstName("Sofia")
                .lastName("Ramirez")
                .build();

        TrainingType type = TrainingType.builder().trainingTypeName("Pilates").build();

        Trainer trainer = Trainer.builder()
                .firstName("Juan")
                .lastName("Perez")
                .trainingType(type)
                .isActive(true)
                .trainees(Set.of(trainee))
                .build();

        TrainerProfileResponseDTO dto = mapper.toProfileDTO(trainer);

        assertEquals("Juan", dto.firstName());
        assertEquals("Pilates", dto.specialization());
        assertEquals("sofia.ramirez", dto.trainees().get(0).username());
    }

    @Test
    void shouldMapToResponseDTOWithTraineeSummaries() {
        Trainee trainee = Trainee.builder()
                .username("miguel.torres")
                .firstName("Miguel")
                .lastName("Torres")
                .build();

        TrainingType type = TrainingType.builder().trainingTypeName("Crossfit").build();

        Trainer trainer = Trainer.builder()
                .username("trainer.luisa")
                .firstName("Luisa")
                .lastName("Gomez")
                .trainingType(type)
                .isActive(true)
                .trainees(Set.of(trainee))
                .build();

        TrainerResponseDTO dto = mapper.toResponseDTO(trainer);

        assertEquals("trainer.luisa", dto.username());
        assertEquals("Crossfit", dto.specialization());
        assertEquals("miguel.torres", dto.trainees().getFirst().username());
    }

    @Test
    void shouldMapTraineeToTraineeSummaryDTO() {
        Trainee trainee = Trainee.builder()
                .username("sofia.ramirez")
                .firstName("Sofia")
                .lastName("Ramirez")
                .build();

        TraineeSummaryResponseDTO dto = mapper.toTraineeSummaryDTO(trainee);

        assertEquals("sofia.ramirez", dto.username());
        assertEquals("Sofia", dto.firstName());
        assertEquals("Ramirez", dto.lastName());
    }
}