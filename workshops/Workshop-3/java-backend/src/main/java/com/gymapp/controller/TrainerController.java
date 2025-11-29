package com.gymapp.controller;

import com.gymapp.dto.request.common.ChangeStatusDTO;
import com.gymapp.dto.request.trainer.CreateTrainerDTO;
import com.gymapp.dto.request.trainer.TrainerTrainingFilterDTO;
import com.gymapp.dto.request.trainer.UpdateTrainerDTO;
import com.gymapp.dto.response.trainer.TraineeSummaryResponseDTO;
import com.gymapp.dto.response.trainer.TrainerCredentialsResponseDTO;
import com.gymapp.dto.response.trainer.TrainerProfileResponseDTO;
import com.gymapp.dto.response.trainer.TrainerResponseDTO;
import com.gymapp.dto.response.training.TrainerTrainingResponseDTO;
import com.gymapp.openapi.annotation.operation.*;
import com.gymapp.service.trainer.TrainerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/trainers")
@Tag(name = "Trainers", description = "Operations related to trainer profiles")
public class TrainerController {

    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }


    @CreateOperation(summary = "Register trainer", description = "Creates a new trainer profile")
    @PostMapping
    public ResponseEntity<TrainerCredentialsResponseDTO> register(@Valid @RequestBody CreateTrainerDTO dto) {
        return new ResponseEntity<>(trainerService.register(dto), HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetByIdOperation(summary = "Get trainer profile", description = "Retrieves the profile of a trainer by username")
    @GetMapping("/{username}")
    public ResponseEntity<TrainerProfileResponseDTO> getProfile(@PathVariable String username) {
        return new ResponseEntity<>(trainerService.getProfile(username), HttpStatus.OK);
    }

    @SecurityRequirement(name = "bearerAuth")
    @UpdateOperation(summary = "Update trainer profile", description = "Updates the profile information of a trainer")
    @PutMapping
    public ResponseEntity<TrainerResponseDTO> updateProfile(@Valid @RequestBody UpdateTrainerDTO dto) {
        return new ResponseEntity<>(trainerService.updateProfile(dto), HttpStatus.OK);
    }

    @SecurityRequirement(name = "bearerAuth")
    @ChangeStatusOperation(summary = "Change trainer status", description = "Activates or deactivates a trainer profile")
    @PatchMapping("/status")
    public ResponseEntity<Void> changeStatus(@Valid @RequestBody ChangeStatusDTO dto) {
        trainerService.changeStatus(dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteOperation(summary = "Delete trainer profile", description = "Deletes a trainer profile by username")
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> delete(@PathVariable String username) {
        trainerService.delete(username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetByIdOperation(summary = "Get assigned trainees",
            description = "Retrieves the list of trainees assigned to a trainer")
    @GetMapping("/{username}/trainees")
    public ResponseEntity<List<TraineeSummaryResponseDTO>> getAssignedTrainees(@PathVariable String username) {
        return new ResponseEntity<>(trainerService.getAssignedTrainees(username), HttpStatus.OK);
    }

    @SecurityRequirement(name = "bearerAuth")
    @SearchTrainingsOperation(summary = "Search trainer trainings", description = "Filters trainer trainings by date range and trainee name")
    @GetMapping("/trainings")
    public ResponseEntity<List<TrainerTrainingResponseDTO>> getTrainingsByCriteria(
            @Valid @ModelAttribute TrainerTrainingFilterDTO filterDTO) {
        return new ResponseEntity<>(trainerService.getTrainingsByCriteria(filterDTO), HttpStatus.OK);
    }
}

