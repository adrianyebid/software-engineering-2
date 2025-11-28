package com.gymapp.service.training;

import com.gymapp.dto.request.training.CreateTrainingDTO;
import com.gymapp.service.training.factory.TrainingFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingFactory trainingFactory;

    public TrainingServiceImpl(TrainingFactory trainingFactory) {
        this.trainingFactory = trainingFactory;
    }

    @Override
    @Transactional
    public void addTraining(CreateTrainingDTO dto) {
        try {
            trainingFactory.create(dto);        }
        catch (Exception e) {
            System.out.println("ERROR EN ADD TRAINING -----------------");
            e.printStackTrace();
            throw e;
        }

    }

}