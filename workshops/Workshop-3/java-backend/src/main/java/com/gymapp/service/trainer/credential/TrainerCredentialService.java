package com.gymapp.service.trainer.credential;

import com.gymapp.model.User;
import com.gymapp.utils.PasswordGenerator;
import com.gymapp.utils.UsernameGenerator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainerCredentialService {
    private static final int PASSWORD_LENGTH = 10;

    public String generateUsername(String firstName, String lastName, List<User> existingUsers) {
        return UsernameGenerator.generateUsername(firstName, lastName, existingUsers);
    }

    public String generatePassword() {
        return PasswordGenerator.generatePassword(PASSWORD_LENGTH);
    }
}