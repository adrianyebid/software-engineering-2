package com.gymapp.service.security.providers;

import com.gymapp.model.User;
import java.util.Optional;

public interface UserProvider {

    Optional<User> findUser(String username);
}