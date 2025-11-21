package com.gymapp.service.trainee;

import com.gymapp.dto.request.common.ChangeStatusDTO;
import com.gymapp.dto.request.trainee.*;
import com.gymapp.dto.response.trainee.TraineeCredentialsResponseDTO;
import com.gymapp.dto.response.trainee.TraineeProfileResponseDTO;
import com.gymapp.dto.response.trainee.TraineeResponseDTO;
import com.gymapp.dto.response.trainee.TrainerSummaryResponseDTO;
import com.gymapp.dto.response.training.TraineeTrainingResponseDTO;
import com.gymapp.mapper.TraineeMapper;
import com.gymapp.model.Trainee;
import com.gymapp.model.Trainer;
import com.gymapp.repository.TraineeRepository;
import com.gymapp.repository.TrainerRepository;
import com.gymapp.service.trainee.factory.TraineeFactory;
import com.gymapp.service.trainee.query.TraineeQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class TraineeServiceImpl implements TraineeService {

    private static final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TraineeMapper traineeMapper;
    private final TraineeFactory traineeFactory;
    private final TraineeQueryService queryService;

    public TraineeServiceImpl(TraineeRepository traineeRepository,
                              TrainerRepository trainerRepository,
                              TraineeMapper traineeMapper,
                              TraineeFactory traineeFactory,
                              TraineeQueryService queryService) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.traineeMapper = traineeMapper;
        this.traineeFactory = traineeFactory;
        this.queryService = queryService;
    }

    @Override
    public TraineeCredentialsResponseDTO register(CreateTraineeDTO dto) {
        logger.info("Registering new trainee: {}", dto.firstName()+" "+dto.lastName());
        TraineeCredentialsResponseDTO credentials = traineeFactory.create(dto);
        return  credentials;
    }

    @Override
    @Transactional(readOnly = true)
    public TraineeProfileResponseDTO getProfile(String username) {
        logger.info("Fetching profile for trainee: {}", username);
        Trainee trainee = findTraineeByUsername(username);
        return traineeMapper.toProfileDTO(trainee);
    }

    @Override
    public TraineeResponseDTO updateProfile(UpdateTraineeDTO dto) {
        logger.info("Updating profile for trainee: {}", dto.username());
        Trainee trainee = findTraineeByUsername(dto.username());
        traineeFactory.update(trainee, dto);
        return traineeMapper.toResponseDTO(trainee);
    }

    @Override
    public void changeStatus(ChangeStatusDTO dto) {
        logger.info("Changing status for trainee: {} to {}", dto.username(), dto.isActive() ? "ACTIVE" : "INACTIVE");
        Trainee trainee = findTraineeByUsername(dto.username());

        if (dto.isActive()) {
            trainee.activate();
        } else {
            trainee.deactivate();
        }

        traineeRepository.save(trainee);
    }

    @Override
    public void delete(String username) {
        logger.info("Deleting trainee: {}", username);
        Trainee trainee = findTraineeByUsername(username);
        traineeFactory.delete(trainee);
    }

    @Override
    public List<TrainerSummaryResponseDTO> assignTrainers(AssignTrainersDTO dto) {
        logger.info("Assigning trainers {} to trainee {}", dto.trainerUsernames(), dto.traineeUsername());
        Trainee trainee = findTraineeByUsername(dto.traineeUsername());
        List<Trainer> trainers = trainerRepository.findAllByUsernameIn(dto.trainerUsernames());
        trainee.setTrainers(new HashSet<>(trainers));
        traineeRepository.save(trainee);
        return traineeMapper.toTrainerSummaryList(trainers);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainerSummaryResponseDTO> getActiveUnassignedTrainers(String username) {
        logger.info("Fetching active unassigned trainers for trainee: {}", username);
        Trainee trainee = findTraineeByUsername(username);
        List<Trainer> unassignedActive = trainerRepository.findActiveTrainersNotAssignedToTrainee(trainee);
        return traineeMapper.toTrainerSummaryList(unassignedActive);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TraineeTrainingResponseDTO> getTrainingsByCriteria(TraineeTrainingFilterDTO filter) {
        logger.info("Fetching trainings for trainee: {} with filters: from={}, to={}, trainer={}, type={}",
                filter.username(), filter.periodFrom(), filter.periodTo(), filter.trainerName(), filter.trainingType());
        return queryService.findTrainingsByCriteria(
                filter.username(),
                filter.periodFrom(),
                filter.periodTo(),
                filter.trainerName(),
                filter.trainingType()
        );
    }

    private Trainee findTraineeByUsername(String username) {
        return traineeRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("Trainee not found with username: {}", username);
                    return new IllegalArgumentException("Trainee no encontrado con username: " + username);
                });
    }


}
