package com.gymapp.service.security;

import com.gymapp.model.Role;
import com.gymapp.repository.TraineeRepository;
import com.gymapp.repository.TrainerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final TrainerRepository trainerRepo;
    private final TraineeRepository traineeRepo;

    public CustomUserDetailsService(TrainerRepository trainerRepo, TraineeRepository traineeRepo) {
        this.trainerRepo = trainerRepo;
        this.traineeRepo = traineeRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return trainerRepo.findByUsername(username)
                .map(t -> buildUserDetails(t.getUsername(), t.getPassword(), t.getIsActive(), Role.TRAINER))
                .or(() -> traineeRepo.findByUsername(username)
                        .map(tr -> buildUserDetails(tr.getUsername(), tr.getPassword(), tr.getIsActive(), Role.TRAINEE)))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    private UserDetails buildUserDetails(String username, String password, Boolean active, Role role) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(password)
                .disabled(!Boolean.TRUE.equals(active))
                .authorities("ROLE_" + role.name())
                .build();
    }
}
