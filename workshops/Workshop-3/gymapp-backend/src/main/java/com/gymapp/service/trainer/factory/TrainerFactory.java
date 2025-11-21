package com.gymapp.service.trainer.factory;

import com.gymapp.dto.request.trainer.CreateTrainerDTO;
import com.gymapp.dto.request.trainer.UpdateTrainerDTO;
import com.gymapp.dto.response.trainer.TrainerCredentialsResponseDTO;
import com.gymapp.mapper.TrainerMapper;
import com.gymapp.model.Trainer;
import com.gymapp.model.TrainingType;
import com.gymapp.model.User;
import com.gymapp.repository.TraineeRepository;
import com.gymapp.repository.TrainerRepository;
import com.gymapp.repository.TrainingTypeRepository;
import com.gymapp.service.trainer.credential.TrainerCredentialService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public class TrainerFactory {

    private final TrainerRepository trainerRepo;
    private final TraineeRepository traineeRepo;
    private final TrainingTypeRepository trainingTypeRepo;
    private final BCryptPasswordEncoder encoder;
    private final TrainerCredentialService credentialService;
    private final TrainerMapper trainerMapper;

    public TrainerFactory(TrainerRepository trainerRepo,
                          TraineeRepository traineeRepo,
                          TrainingTypeRepository trainingTypeRepo,
                          BCryptPasswordEncoder encoder,
                          TrainerCredentialService credentialService,
                          TrainerMapper trainerMapper) {
        this.trainerRepo = trainerRepo;
        this.traineeRepo = traineeRepo;
        this.trainingTypeRepo = trainingTypeRepo;
        this.encoder = encoder;
        this.credentialService = credentialService;
        this.trainerMapper = trainerMapper;
    }

    public TrainerCredentialsResponseDTO create(CreateTrainerDTO dto) {

        boolean traineeExists = traineeRepo.existsByFirstNameAndLastName(dto.firstName(), dto.lastName());
        if (traineeExists) {
            throw new IllegalArgumentException("A trainee with the same first name and last name already exists.");
        }
        List<User> existingUsers = Stream.concat(trainerRepo.findAll().stream(), traineeRepo.findAll().stream()).toList();

        String username = credentialService.generateUsername(dto.firstName(), dto.lastName(), existingUsers);
        String rawPassword = credentialService.generatePassword();
        String encodedPassword = encoder.encode(rawPassword);

        TrainingType trainingType = trainingTypeRepo.findByTrainingTypeName(dto.specialization())
                .orElseThrow(() -> new RuntimeException("TrainingType no encontrado"));

        Trainer trainer = trainerMapper.toEntity(dto, trainingType);
        trainer.setUsername(username);
        trainer.setPassword(encodedPassword);
        trainer.setIsActive(true);

        trainerRepo.save(trainer);

        return new TrainerCredentialsResponseDTO(username,rawPassword);
    }

    public void update(Trainer trainer, UpdateTrainerDTO dto) {
        trainerMapper.updateEntity(trainer, dto);
        trainerRepo.save(trainer);
    }

    public void delete(Trainer trainer) {
        trainerRepo.delete(trainer);
    }
}