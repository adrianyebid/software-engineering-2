package com.gymapp.repository;

import com.gymapp.model.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {

    Optional<Trainee> findByUsername(String username);

    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    @Query("SELECT COUNT(t) FROM Trainee t WHERE t.isActive = false")
    long countInactiveTrainees();

}
