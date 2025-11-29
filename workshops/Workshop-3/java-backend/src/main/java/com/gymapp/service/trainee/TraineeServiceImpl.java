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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class TraineeServiceImpl implements TraineeService {

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
        return traineeFactory.create(dto);
    }

    @Override
    @Transactional(readOnly = true)
    public TraineeProfileResponseDTO getProfile(String username) {
        Trainee trainee = findTraineeByUsername(username);
        return traineeMapper.toProfileDTO(trainee);
    }

    @Override
    public TraineeResponseDTO updateProfile(UpdateTraineeDTO dto) {
        Trainee trainee = findTraineeByUsername(dto.username());
        traineeFactory.update(trainee, dto);
        return traineeMapper.toResponseDTO(trainee);
    }

    @Override
    public void changeStatus(ChangeStatusDTO dto) {
        Trainee trainee = findTraineeByUsername(dto.username());

        if (dto.isActive()) trainee.activate();
        else trainee.deactivate();

        traineeRepository.save(trainee);
    }



    @Override
    public void delete(String username) {
        Trainee trainee = findTraineeByUsername(username);
        traineeFactory.delete(trainee);
    }

    @Override
    public List<TrainerSummaryResponseDTO> assignTrainers(AssignTrainersDTO dto) {
        Trainee trainee = findTraineeByUsername(dto.traineeUsername());
        List<Trainer> trainers = validateTrainers(dto.trainerUsernames());
        trainee.getTrainers().clear();
        trainee.getTrainers().addAll(trainers);
        traineeRepository.save(trainee);
        return traineeMapper.toTrainerSummaryList(trainers);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainerSummaryResponseDTO> getActiveUnassignedTrainers(String username) {
        Trainee trainee = findTraineeByUsername(username);
        List<Trainer> unassignedActive = trainerRepository.findActiveTrainersNotAssignedToTrainee(trainee);
        return traineeMapper.toTrainerSummaryList(unassignedActive);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TraineeTrainingResponseDTO> getTrainingsByCriteria(TraineeTrainingFilterDTO filter) {

        return queryService.findTrainingsByCriteria(
                filter.getUsername(),
                filter.getPeriodFrom(),
                filter.getPeriodTo(),
                filter.getTrainerUsername(),
                filter.getTrainingType()
        );
    }

    private Trainee findTraineeByUsername(String username) {
        return traineeRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Trainee no encontrado con username: " + username));
    }

    private List<Trainer> validateTrainers(List<String> usernames) {
        List<Trainer> trainers = trainerRepository.findAllByUsernameIn(usernames);

        if (trainers.size() != usernames.size()) {

            Set<String> found = trainers.stream()
                    .map(Trainer::getUsername)
                    .collect(Collectors.toSet());

            List<String> missing = usernames.stream()
                    .filter(username -> !found.contains(username))
                    .toList();

            throw new IllegalArgumentException("Los siguientes trainers no existen: " + missing);
        }

        return trainers;
    }






}
