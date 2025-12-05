package com.gymapp.service.security.providers;

import com.gymapp.model.User;
import com.gymapp.repository.TraineeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class TraineeUserProvider implements UserProvider {

    private final TraineeRepository traineeRepo;

    @Override
    public Optional<User> findUser(String username) {
        return traineeRepo.findByUsername(username)
                .map(Function.identity());
    }
}