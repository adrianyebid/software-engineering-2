package com.gymapp.service.trainee.factory;

import com.gymapp.dto.request.trainee.CreateTraineeDTO;
import com.gymapp.dto.request.trainee.UpdateTraineeDTO;
import com.gymapp.mapper.TraineeMapper;
import com.gymapp.model.Trainee;
import com.gymapp.repository.TraineeRepository;
import com.gymapp.repository.TrainerRepository;
import com.gymapp.service.trainee.credential.TraineeCredentialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeFactoryTest {

    private TraineeRepository traineeRepo;
    private TrainerRepository trainerRepo;
    private BCryptPasswordEncoder encoder;
    private TraineeCredentialService credentialService;
    private TraineeMapper traineeMapper;
    private TraineeFactory factory;

    @BeforeEach
    void setUp() {
        traineeRepo = mock(TraineeRepository.class);
        trainerRepo = mock(TrainerRepository.class);
        encoder = mock(BCryptPasswordEncoder.class);
        credentialService = mock(TraineeCredentialService.class);
        traineeMapper = mock(TraineeMapper.class);
        factory = new TraineeFactory(traineeRepo, trainerRepo, encoder, credentialService, traineeMapper);
    }

    @Test
    void shouldCreateTraineeWithEncodedPasswordAndGeneratedUsername() {
        CreateTraineeDTO dto = new CreateTraineeDTO("Sofia", "Ramirez", LocalDate.of(1995, 5, 15), "456 Elm St");

        when(trainerRepo.existsByFirstNameAndLastName("Sofia", "Ramirez")).thenReturn(false);
        when(traineeRepo.findAll()).thenReturn(List.of());
        when(trainerRepo.findAll()).thenReturn(List.of());
        when(credentialService.generateUsername("Sofia", "Ramirez", List.of())).thenReturn("sofia.ramirez");
        when(credentialService.generatePassword()).thenReturn("rawpass");
        when(encoder.encode("rawpass")).thenReturn("encodedpass");

        Trainee mapped = Trainee.builder().build();
        when(traineeMapper.toEntity(dto)).thenReturn(mapped);
        when(traineeRepo.save(mapped)).thenReturn(mapped);

        Trainee result = factory.create(dto);

        assertEquals("sofia.ramirez", result.getUsername());
        assertEquals("rawpass", result.getPassword()); // raw password is set after save
        verify(traineeRepo).save(mapped);
    }

    @Test
    void shouldThrowIfTrainerWithSameNameExists() {
        CreateTraineeDTO dto = new CreateTraineeDTO("Sofia", "Ramirez", LocalDate.of(1995, 5, 15), "456 Elm St");

        when(trainerRepo.existsByFirstNameAndLastName("Sofia", "Ramirez")).thenReturn(true);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> factory.create(dto));
        assertEquals("A trainer with the same first name and last name already exists.", ex.getMessage());
    }

    @Test
    void shouldUpdateTraineeFields() {
        Trainee trainee = Trainee.builder()
                .firstName("Old")
                .lastName("Name")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("Old Address")
                .build();

        UpdateTraineeDTO dto = new UpdateTraineeDTO(
                "sofia.ramirez", // username
                "New",            // firstName
                "Name",           // lastName
                LocalDate.of(2000, 1, 1), // dateOfBirth
                "New Address",    // address
                true              // isActive
        );
        doAnswer(invocation -> {
            Trainee t = invocation.getArgument(0);
            t.setFirstName(dto.firstName());
            t.setLastName(dto.lastName());
            t.setDateOfBirth(dto.dateOfBirth());
            t.setAddress(dto.address());
            return null;
        }).when(traineeMapper).updateEntity(trainee, dto);

        factory.update(trainee, dto);

        assertEquals("New", trainee.getFirstName());
        assertEquals("New Address", trainee.getAddress());
        assertEquals(LocalDate.of(2000, 1, 1), trainee.getDateOfBirth());
        verify(traineeRepo).save(trainee);
    }

    @Test
    void shouldDeleteTrainee() {
        Trainee trainee = Trainee.builder().username("sofia.ramirez").build();
        factory.delete(trainee);
        verify(traineeRepo).delete(trainee);
    }
}