package com.gymapp.facade;

import com.gymapp.dto.request.common.ChangeStatusDTO;
import com.gymapp.dto.request.trainee.*;
import com.gymapp.dto.request.trainer.*;
import com.gymapp.dto.request.training.CreateTrainingDTO;
import com.gymapp.dto.response.trainee.*;
import com.gymapp.dto.response.trainer.*;
import com.gymapp.dto.response.training.*;
import com.gymapp.dto.response.trainingtype.TrainingTypeResponseDTO;
import com.gymapp.service.trainee.TraineeService;
import com.gymapp.service.trainer.TrainerService;
import com.gymapp.service.training.TrainingService;
import com.gymapp.service.trainingtype.TrainingTypeService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GymAppFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;

    public GymAppFacade(
            TraineeService traineeService,
            TrainerService trainerService,
            TrainingService trainingService,
            TrainingTypeService trainingTypeService
    ) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
        this.trainingTypeService = trainingTypeService;
    }

    // ───── Trainee ─────

    public TraineeCredentialsResponseDTO registerTrainee(CreateTraineeDTO dto) {
        return traineeService.register(dto);
    }

    public TraineeProfileResponseDTO getTraineeProfile(String username) {
        return traineeService.getProfile(username);
    }

    public TraineeResponseDTO updateTraineeProfile(UpdateTraineeDTO dto) {
        return traineeService.updateProfile(dto);
    }

    public void changeTraineeStatus(ChangeStatusDTO dto) {
        traineeService.changeStatus(dto);
    }

    public void deleteTrainee(String username) {
        traineeService.delete(username);
    }

    public List<TrainerSummaryResponseDTO> assignTrainersToTrainee(AssignTrainersDTO dto) {
        return traineeService.assignTrainers(dto);
    }

    public List<TrainerSummaryResponseDTO> getUnassignedTrainersForTrainee(String username) {
        return traineeService.getActiveUnassignedTrainers(username);
    }

    public List<TraineeTrainingResponseDTO> getTraineeTrainings(TraineeTrainingFilterDTO filter) {
        return traineeService.getTrainingsByCriteria(filter);
    }

    // ───── Trainer ─────

    public TrainerCredentialsResponseDTO registerTrainer(CreateTrainerDTO dto) {
        return trainerService.register(dto);
    }

    public TrainerProfileResponseDTO getTrainerProfile(String username) {
        return trainerService.getProfile(username);
    }

    public TrainerResponseDTO updateTrainerProfile(UpdateTrainerDTO dto) {
        return trainerService.updateProfile(dto);
    }

    public void changeTrainerStatus(ChangeStatusDTO dto) {
        trainerService.changeStatus(dto);
    }

    public void deleteTrainer(String username) {
        trainerService.delete(username);
    }

    public List<TraineeSummaryResponseDTO> getTraineesAssignedToTrainer(String username) {
        return trainerService.getAssignedTrainees(username);
    }

    public List<TrainerTrainingResponseDTO> getTrainerTrainings(TrainerTrainingFilterDTO filter) {
        return trainerService.getTrainingsByCriteria(filter);
    }

    // ───── Training ─────

    public void addTraining(CreateTrainingDTO dto) {
        trainingService.addTraining(dto);
    }

    // ───── Training Types ─────

    public List<TrainingTypeResponseDTO> getAllTrainingTypes() {
        return trainingTypeService.getAllTrainingTypes();
    }
}