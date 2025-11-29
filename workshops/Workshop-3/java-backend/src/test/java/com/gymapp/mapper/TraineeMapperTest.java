package com.gymapp.mapper;

import com.gymapp.dto.request.trainee.CreateTraineeDTO;
import com.gymapp.dto.request.trainee.UpdateTraineeDTO;
import com.gymapp.dto.response.trainee.TraineeCredentialsResponseDTO;
import com.gymapp.dto.response.trainee.TraineeProfileResponseDTO;
import com.gymapp.dto.response.trainee.TraineeResponseDTO;
import com.gymapp.dto.response.trainee.TrainerSummaryResponseDTO;
import com.gymapp.model.Trainee;
import com.gymapp.model.Trainer;
import com.gymapp.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TraineeMapperTest {

    private TraineeMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TraineeMapper();
    }

    @Test
    void shouldMapCreateTraineeDTOToEntity() {
        CreateTraineeDTO dto = new CreateTraineeDTO("Sofia", "Ramirez", LocalDate.of(1995, 5, 15), "456 Elm St");

        Trainee trainee = mapper.toEntity(dto);

        assertEquals("Sofia", trainee.getFirstName());
        assertEquals("Ramirez", trainee.getLastName());
        assertEquals(LocalDate.of(1995, 5, 15), trainee.getDateOfBirth());
        assertEquals("456 Elm St", trainee.getAddress());
    }

    @Test
    void shouldUpdateTraineeEntityFromUpdateDTO() {
        Trainee trainee = Trainee.builder()
                .firstName("Old")
                .lastName("Name")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("Old Address")
                .isActive(false)
                .build();

        UpdateTraineeDTO dto = new UpdateTraineeDTO("sofia.ramirez", "New", "Name", LocalDate.of(2000, 1, 1), "New Address", true);

        mapper.updateEntity(trainee, dto);

        assertEquals("New", trainee.getFirstName());
        assertEquals("Name", trainee.getLastName());
        assertEquals(LocalDate.of(2000, 1, 1), trainee.getDateOfBirth());
        assertEquals("New Address", trainee.getAddress());
        assertTrue(trainee.getIsActive());
    }

    @Test
    void shouldMapToCredentialsDTO() {
        Trainee trainee = Trainee.builder()
                .username("sofia.ramirez")
                .password("encodedpass")
                .build();

        TraineeCredentialsResponseDTO dto = mapper.toCredentialsDTO(trainee);

        assertEquals("sofia.ramirez", dto.username());
        assertEquals("encodedpass", dto.password());
    }

    @Test
    void shouldMapToProfileDTOWithTrainerSummaries() {
        TrainingType type = TrainingType.builder().trainingTypeName("Yoga").build();
        Trainer trainer = Trainer.builder()
                .username("trainer.juan")
                .firstName("Juan")
                .lastName("Perez")
                .trainingType(type)
                .build();

        Trainee trainee = Trainee.builder()
                .firstName("Sofia")
                .lastName("Ramirez")
                .dateOfBirth(LocalDate.of(1995, 5, 15))
                .address("456 Elm St")
                .isActive(true)
                .trainers(Set.of(trainer))
                .build();

        TraineeProfileResponseDTO dto = mapper.toProfileDTO(trainee);

        assertEquals("Sofia", dto.firstName());
        assertEquals("Ramirez", dto.lastName());
        assertEquals("Yoga", dto.trainers().getFirst().specialization());    }

    @Test
    void shouldMapToResponseDTOWithTrainerSummaries() {
        TrainingType type = TrainingType.builder().trainingTypeName("Pilates").build();
        Trainer trainer = Trainer.builder()
                .username("trainer.luisa")
                .firstName("Luisa")
                .lastName("Gomez")
                .trainingType(type)
                .build();

        Trainee trainee = Trainee.builder()
                .username("sofia.ramirez")
                .firstName("Sofia")
                .lastName("Ramirez")
                .dateOfBirth(LocalDate.of(1995, 5, 15))
                .address("456 Elm St")
                .isActive(true)
                .trainers(Set.of(trainer))
                .build();

        TraineeResponseDTO dto = mapper.toResponseDTO(trainee);

        assertEquals("sofia.ramirez", dto.username());
        assertEquals("Pilates", dto.trainers().getFirst().specialization());    }

    @Test
    void shouldMapTrainerToTrainerSummaryDTO() {
        TrainingType type = TrainingType.builder().trainingTypeName("Crossfit").build();
        Trainer trainer = Trainer.builder()
                .username("trainer.maria")
                .firstName("Maria")
                .lastName("Lopez")
                .trainingType(type)
                .build();

        TrainerSummaryResponseDTO dto = mapper.toTrainerSummaryDTO(trainer);

        assertEquals("trainer.maria", dto.username());
        assertEquals("Maria", dto.firstName());
        assertEquals("Lopez", dto.lastName());
        assertEquals("Crossfit", dto.specialization());
    }
}