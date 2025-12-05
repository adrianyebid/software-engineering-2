package com.gymapp.service.security.providers;

import com.gymapp.model.User;
import com.gymapp.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class TrainerUserProvider implements UserProvider {

    private final TrainerRepository trainerRepo;

    @Override
    public Optional<User> findUser(String username) {
        return trainerRepo.findByUsername(username)
                .map(Function.identity());
    }
}