package com.gymapp.service.trainer.factory;

import com.gymapp.dto.request.trainer.CreateTrainerDTO;
import com.gymapp.dto.request.trainer.UpdateTrainerDTO;
import com.gymapp.dto.response.trainer.TrainerCredentialsResponseDTO;
import com.gymapp.mapper.TrainerMapper;
import com.gymapp.model.Trainer;
import com.gymapp.model.TrainingType;
import com.gymapp.repository.TraineeRepository;
import com.gymapp.repository.TrainerRepository;
import com.gymapp.repository.TrainingTypeRepository;
import com.gymapp.service.trainer.credential.TrainerCredentialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerFactoryTest {

    private TrainerRepository trainerRepo;
    private TraineeRepository traineeRepo;
    private TrainingTypeRepository trainingTypeRepo;
    private BCryptPasswordEncoder encoder;
    private TrainerCredentialService credentialService;
    private TrainerMapper trainerMapper;
    private TrainerFactory factory;

    @BeforeEach
    void setUp() {
        trainerRepo = mock(TrainerRepository.class);
        traineeRepo = mock(TraineeRepository.class);
        trainingTypeRepo = mock(TrainingTypeRepository.class);
        encoder = mock(BCryptPasswordEncoder.class);
        credentialService = mock(TrainerCredentialService.class);
        trainerMapper = mock(TrainerMapper.class);
        factory = new TrainerFactory(trainerRepo, traineeRepo, trainingTypeRepo, encoder, credentialService, trainerMapper);
    }

    @Test
    void shouldCreateTrainerWithEncodedPasswordAndTrainingType() {
        CreateTrainerDTO dto = new CreateTrainerDTO("Ana", "Lopez", "Yoga");

        when(traineeRepo.existsByFirstNameAndLastName("Ana", "Lopez")).thenReturn(false);
        when(trainerRepo.findAll()).thenReturn(List.of());
        when(traineeRepo.findAll()).thenReturn(List.of());
        when(credentialService.generateUsername("Ana", "Lopez", List.of())).thenReturn("ana.lopez");
        when(credentialService.generatePassword()).thenReturn("rawpass");
        when(encoder.encode("rawpass")).thenReturn("encodedpass");

        // El normalizeSpecialization("Yoga") -> "YOGA"
        TrainingType type = TrainingType.builder().trainingTypeName("YOGA").build();
        when(trainingTypeRepo.findByTrainingTypeNameIgnoreCase("YOGA")).thenReturn(Optional.of(type));

        Trainer mapped = Trainer.builder().trainingType(type).build();
        when(trainerMapper.toEntity(dto, type)).thenReturn(mapped);

        TrainerCredentialsResponseDTO result = factory.create(dto);

        assertEquals("ana.lopez", result.username());
        assertEquals("rawpass", result.password());

        verify(trainerRepo).save(argThat(trainer ->
                trainer.getUsername().equals("ana.lopez") &&
                        trainer.getPassword().equals("encodedpass") &&
                        trainer.getTrainingType().getTrainingTypeName().equals("YOGA") &&
                        trainer.getIsActive()
        ));
    }

    @Test
    void shouldThrowIfTraineeWithSameNameExists() {
        CreateTrainerDTO dto = new CreateTrainerDTO("Ana", "Lopez", "Yoga");

        when(traineeRepo.existsByFirstNameAndLastName("Ana", "Lopez")).thenReturn(true);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> factory.create(dto));
        assertEquals("A trainee with the same first name and last name already exists.", ex.getMessage());
    }

    @Test
    void shouldThrowIfTrainingTypeNotFound() {
        CreateTrainerDTO dto = new CreateTrainerDTO("Ana", "Lopez", "Zumba");

        when(traineeRepo.existsByFirstNameAndLastName("Ana", "Lopez")).thenReturn(false);
        when(trainerRepo.findAll()).thenReturn(List.of());
        when(traineeRepo.findAll()).thenReturn(List.of());
        when(credentialService.generateUsername(any(), any(), any())).thenReturn("ana.lopez");
        when(credentialService.generatePassword()).thenReturn("rawpass");
        when(encoder.encode("rawpass")).thenReturn("encodedpass");

        // normalizeSpecialization("Zumba") -> "ZUMBA"
        when(trainingTypeRepo.findByTrainingTypeNameIgnoreCase("ZUMBA")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> factory.create(dto));
        assertEquals("Specialization 'Zumba' does not exist", ex.getMessage());
    }

    @Test
    void shouldUpdateTrainerFields() {
        Trainer trainer = Trainer.builder()
                .firstName("Old")
                .lastName("Name")
                .isActive(false)
                .build();

        UpdateTrainerDTO dto = new UpdateTrainerDTO("ana.lopez", "New", "Name", true);

        doAnswer(invocation -> {
            Trainer t = invocation.getArgument(0);
            t.setFirstName(dto.firstName());
            t.setLastName(dto.lastName());
            t.setIsActive(dto.isActive());
            return null;
        }).when(trainerMapper).updateEntity(trainer, dto);

        factory.update(trainer, dto);

        assertEquals("New", trainer.getFirstName());
        assertEquals("Name", trainer.getLastName());
        assertTrue(trainer.getIsActive());
        verify(trainerRepo).save(trainer);
    }

    @Test
    void shouldDeleteTrainer() {
        Trainer trainer = Trainer.builder().username("ana.lopez").build();
        factory.delete(trainer);
        verify(trainerRepo).delete(trainer);
    }
}
