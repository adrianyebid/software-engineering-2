package com.gymapp.utils;

import com.gymapp.model.User;

import java.util.List;
import java.util.stream.IntStream;

public class UsernameGenerator {

    private UsernameGenerator() {
        throw new IllegalStateException("Utility class");
    }

    public static String generateUsername(String firstName, String lastName, List<User> existingUsers) {
        String base = (firstName + "." + lastName).toLowerCase();

        // Encuentra el primer número que haga único el username
        int suffix = IntStream.iterate(0, i -> i + 1)
                .filter(i -> {
                    String username = (i == 0) ? base : base + i;
                    return existingUsers.stream()
                            .noneMatch(u -> username.equals(u.getUsername()));
                })
                .findFirst()
                .orElse(0);

        return (suffix == 0) ? base : base + suffix;
    }
}
