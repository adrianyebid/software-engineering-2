package com.gymapp.controller;

import com.gymapp.dto.response.trainingtype.TrainingTypeResponseDTO;
import com.gymapp.openapi.annotation.operation.GetAllOperation;
import com.gymapp.service.trainingtype.TrainingTypeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/training-types")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Training Types", description = "Operations related to available training types")
public class TrainingTypeController {

    private final TrainingTypeService trainingTypeService;

    public TrainingTypeController(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }

    @GetAllOperation(summary = "Get all training types", description = "Retrieves the list of all available training types")
    @GetMapping
    public ResponseEntity<List<TrainingTypeResponseDTO>> getAllTrainingTypes() {
        return new ResponseEntity<>(trainingTypeService.getAllTrainingTypes(), HttpStatus.OK);
    }
}