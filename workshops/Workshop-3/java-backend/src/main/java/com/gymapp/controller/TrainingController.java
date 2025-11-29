package com.gymapp.controller;

import com.gymapp.dto.request.training.CreateTrainingDTO;
import com.gymapp.openapi.annotation.operation.TrainingCreateOperation;
import com.gymapp.service.training.TrainingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trainings")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Trainings", description = "Operations related to training creation and management")
public class TrainingController {

    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @TrainingCreateOperation()
    @PostMapping
    public ResponseEntity<Void> addTraining(@Valid @RequestBody CreateTrainingDTO dto) {
        trainingService.addTraining(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}