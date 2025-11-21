package com.gymapp.service.core;

import com.gymapp.dto.request.auth.ChangeLoginRequestDTO;
import com.gymapp.dto.request.auth.LoginRequestDTO;
import com.gymapp.model.Trainer;
import com.gymapp.repository.TrainerRepository;
import com.gymapp.utils.PasswordVerifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TrainerSecurityHandler implements UserSecurityHandler {

    private final TrainerRepository trainerRepository;
    private final PasswordVerifier passwordVerifier;
    private final PasswordEncoder passwordEncoder;

    public TrainerSecurityHandler(TrainerRepository trainerRepository,
                                  PasswordVerifier passwordVerifier,
                                  PasswordEncoder passwordEncoder) {
        this.trainerRepository = trainerRepository;
        this.passwordVerifier = passwordVerifier;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean supports(String username) {
        return trainerRepository.findByUsername(username).isPresent();
    }

    @Override
    public void authenticate(LoginRequestDTO dto) {
        Trainer trainer = trainerRepository.findByUsername(dto.username())
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found"));
        passwordVerifier.verifyOrThrow(dto.password(), trainer.getPassword());
    }

    @Override
    public void changePassword(ChangeLoginRequestDTO dto) {
        Trainer trainer = trainerRepository.findByUsername(dto.username())
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found"));
        passwordVerifier.verifyOrThrow(dto.oldPassword(), trainer.getPassword());
        trainer.setPassword(passwordEncoder.encode(dto.newPassword()));
        trainerRepository.save(trainer);
    }
}
