package com.gymapp.service.trainee.factory;

import com.gymapp.dto.response.trainee.TraineeCredentialsResponseDTO;
import com.gymapp.mapper.TraineeMapper;
import com.gymapp.model.Trainee;
import com.gymapp.model.User;
import com.gymapp.repository.TraineeRepository;
import com.gymapp.repository.TrainerRepository;
import com.gymapp.service.trainee.credential.TraineeCredentialService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;


import com.gymapp.dto.request.trainee.CreateTraineeDTO;
import com.gymapp.dto.request.trainee.UpdateTraineeDTO;


@Component
public class TraineeFactory {

    private final TraineeRepository traineeRepo;
    private final TrainerRepository trainerRepo;
    private final BCryptPasswordEncoder encoder;
    private final TraineeCredentialService credentialService;
    private final TraineeMapper traineeMapper;

    public TraineeFactory(TraineeRepository traineeRepo,
                          TrainerRepository trainerRepo,
                          BCryptPasswordEncoder encoder,
                          TraineeCredentialService credentialService,
                          TraineeMapper traineeMapper) {
        this.traineeRepo = traineeRepo;
        this.trainerRepo = trainerRepo;
        this.encoder = encoder;
        this.credentialService = credentialService;
        this.traineeMapper = traineeMapper;
    }

    public TraineeCredentialsResponseDTO create(CreateTraineeDTO dto) {

        //evitar que exista un trainer con el mismo nombre y apellido
        boolean trainerExists = trainerRepo.existsByFirstNameAndLastName(dto.firstName(), dto.lastName());
        if (trainerExists) {
            throw new IllegalArgumentException("A trainer with the same first name and last name already exists.");
        }

        List<User> existingUsers = Stream.concat(traineeRepo.findAll().stream(), trainerRepo.findAll().stream()).toList();

        String username = credentialService.generateUsername(dto.firstName(), dto.lastName(), existingUsers);
        String rawPassword = credentialService.generatePassword();
        String encodedPassword = encoder.encode(rawPassword);

        Trainee trainee = traineeMapper.toEntity(dto);
        trainee.setUsername(username);
        trainee.setPassword(encodedPassword);
        trainee.setIsActive(true);

        traineeRepo.save(trainee);

        return new TraineeCredentialsResponseDTO(username, rawPassword);
    }

    public void update(Trainee trainee, UpdateTraineeDTO dto) {
        traineeMapper.updateEntity(trainee, dto);
        traineeRepo.save(trainee);
    }

    public void delete(Trainee trainee) {
        traineeRepo.delete(trainee);
    }
}