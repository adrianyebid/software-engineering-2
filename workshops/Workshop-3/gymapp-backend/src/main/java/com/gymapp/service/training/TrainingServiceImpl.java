package com.gymapp.service.training;

import com.gymapp.dto.request.training.CreateTrainingDTO;
import com.gymapp.service.training.factory.TrainingFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrainingServiceImpl implements TrainingService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);

    private final TrainingFactory trainingFactory;

    public TrainingServiceImpl(TrainingFactory trainingFactory) {
        this.trainingFactory = trainingFactory;
    }

    @Override
    @Transactional
    public void addTraining(CreateTrainingDTO dto) {
        logger.info("Creating training for trainee={} with trainer={} and type={}",
                dto.traineeUsername(), dto.trainerUsername(), dto.trainingName());
        trainingFactory.create(dto);
    }

}