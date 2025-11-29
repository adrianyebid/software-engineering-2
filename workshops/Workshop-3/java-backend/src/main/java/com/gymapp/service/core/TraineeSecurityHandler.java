package com.gymapp.service.core;

import com.gymapp.dto.request.auth.ChangeLoginRequestDTO;
import com.gymapp.dto.request.auth.LoginRequestDTO;
import com.gymapp.repository.TraineeRepository;
import com.gymapp.utils.PasswordVerifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
@Component
public class TraineeSecurityHandler implements UserSecurityHandler {

    private final TraineeRepository traineeRepository;
    private final PasswordVerifier passwordVerifier;
    private final PasswordEncoder passwordEncoder;

    public TraineeSecurityHandler(TraineeRepository traineeRepository,
                                  PasswordVerifier passwordVerifier,
                                  PasswordEncoder passwordEncoder) {
        this.traineeRepository = traineeRepository;
        this.passwordVerifier = passwordVerifier;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean supports(String username) {
        return traineeRepository.findByUsername(username).isPresent();
    }

    @Override
    public void authenticate(LoginRequestDTO dto) {
        var trainee = traineeRepository.findByUsername(dto.username())
                .orElseThrow(() -> new IllegalArgumentException("Trainee not found"));
        passwordVerifier.verifyOrThrow(dto.password(), trainee.getPassword());
    }

    @Override
    public void changePassword(ChangeLoginRequestDTO dto) {
        var trainee = traineeRepository.findByUsername(dto.username())
                .orElseThrow(() -> new IllegalArgumentException("Trainee not found"));
        passwordVerifier.verifyOrThrow(dto.oldPassword(), trainee.getPassword());
        trainee.setPassword(passwordEncoder.encode(dto.newPassword()));
        traineeRepository.save(trainee);
    }
}