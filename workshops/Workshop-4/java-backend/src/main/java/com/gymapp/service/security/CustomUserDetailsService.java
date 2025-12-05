package com.gymapp.service.security;

import com.gymapp.model.User;
import com.gymapp.service.security.providers.UserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    // Inyección de la lista de estrategias
    private final List<UserProvider> userProviders;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserInRepositories(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return mapToUserDetails(user);
    }

    private Optional<User> findUserInRepositories(String username) {
        return userProviders.stream()
                .map(provider -> provider.findUser(username))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    private UserDetails mapToUserDetails(User user) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .disabled(!Boolean.TRUE.equals(user.getIsActive()))
                .authorities("ROLE_" + user.getRole().name()) // Polimorfismo
                .build();
    }
}