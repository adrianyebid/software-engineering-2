package com.gymapp.repository;

import com.gymapp.model.Trainee;
import com.gymapp.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    Optional<Trainer> findByUsername(String username);

    boolean existsByFirstNameAndLastName(String firstName, String lastName);


    @Query("SELECT t FROM Trainer t WHERE :trainee NOT MEMBER OF t.trainees AND t.isActive = true")
    List<Trainer> findActiveTrainersNotAssignedToTrainee(@Param("trainee") Trainee trainee);

    @Query("SELECT t FROM Trainer t WHERE t.isActive = true AND t.trainees IS EMPTY")
    List<Trainer> findActiveTrainersNotAssignedToAnyTrainee();

    List<Trainer> findAllByUsernameIn(List<String> usernames);

}