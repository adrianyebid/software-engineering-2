package com.gymapp.controller;

import com.gymapp.dto.request.common.ChangeStatusDTO;
import com.gymapp.dto.request.trainee.*;
import com.gymapp.dto.response.trainee.*;
import com.gymapp.dto.response.training.TraineeTrainingResponseDTO;
import com.gymapp.openapi.annotation.operation.*;
import com.gymapp.service.trainee.TraineeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/trainees")
@Tag(name = "Trainees", description = "Operations related to trainee profiles")
public class TraineeController {

    private final TraineeService traineeService;

    public TraineeController(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @CreateOperation(summary = "Register trainee", description = "Creates a new trainee profile")
    @PostMapping
    public ResponseEntity<TraineeCredentialsResponseDTO> register(@Valid @RequestBody CreateTraineeDTO dto) {
        return new ResponseEntity<>(traineeService.register(dto), HttpStatus.CREATED);
    }

    @GetByIdOperation(summary = "Get trainee profile", description = "Retrieves the profile of a trainee by username")
    @GetMapping("/{username}")
    public ResponseEntity<TraineeProfileResponseDTO> getProfile(@PathVariable String username) {
        return new ResponseEntity<>(traineeService.getProfile(username), HttpStatus.OK);
    }

    @UpdateOperation(summary = "Update trainee profile", description = "Updates the profile information of a trainee")
    @PutMapping
    public ResponseEntity<TraineeResponseDTO> updateProfile(@Valid @RequestBody UpdateTraineeDTO dto) {
        return new ResponseEntity<>(traineeService.updateProfile(dto), HttpStatus.OK);
    }

    @DeleteOperation(summary = "Delete trainee profile", description = "Deletes a trainee profile by username")
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> delete(@PathVariable String username) {
        traineeService.delete(username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetByIdOperation(summary = "Get unassigned trainers", description = "Retrieves active trainers not assigned to the trainee")
    @GetMapping("/{username}/trainers/not-assigned")
    public ResponseEntity<List<TrainerSummaryResponseDTO>> getActiveUnassignedTrainers(@PathVariable String username) {
        return new ResponseEntity<>(traineeService.getActiveUnassignedTrainers(username), HttpStatus.OK);
    }

    @AssignOperation(summary = "Assign trainers", description = "Assigns a list of trainers to a trainee")
    @PutMapping("/trainers")
    public ResponseEntity<List<TrainerSummaryResponseDTO>> assignTrainers(@Valid @RequestBody AssignTrainersDTO dto) {
        return new ResponseEntity<>(traineeService.assignTrainers(dto), HttpStatus.OK);
    }

    @SearchTrainingsOperation(summary = "Search trainee trainings", description = "Filters trainee trainings by date range, trainer name, and training type")
    @GetMapping("/trainings")
    public ResponseEntity<List<TraineeTrainingResponseDTO>> getTrainingsByCriteria(
            @RequestParam String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodTo,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) String trainingType
    ) {
        TraineeTrainingFilterDTO dto = new TraineeTrainingFilterDTO(username, periodFrom, periodTo, trainerName, trainingType);
        return new ResponseEntity<>(traineeService.getTrainingsByCriteria(dto), HttpStatus.OK);
    }

    @ChangeStatusOperation(summary = "Change trainee status", description = "Activates or deactivates a trainee profile")
    @PatchMapping("/status")
    public ResponseEntity<Void> changeStatus(@Valid @RequestBody ChangeStatusDTO dto) {
        traineeService.changeStatus(dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

