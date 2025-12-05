package com.gymapp.service.security;

import com.gymapp.model.Trainee; // Importación necesaria
import com.gymapp.model.Trainer; // Importación necesaria
import com.gymapp.service.security.providers.UserProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomUserDetailsService Unit Tests")
class CustomUserDetailsServiceTest {

    @Mock
    private UserProvider traineeProvider;

    @Mock
    private UserProvider trainerProvider;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private static final String TRAINEE_USERNAME = "trainee_user";
    private static final String TRAINER_USERNAME = "trainer_user";
    private static final String UNKNOWN_USERNAME = "unknown";

    private Trainee activeTrainee;
    private Trainer inactiveTrainer;

    @BeforeEach
    void setUp() {
        customUserDetailsService = new CustomUserDetailsService(List.of(traineeProvider, trainerProvider));

        activeTrainee = Trainee.builder()
                .id(1L)
                .username(TRAINEE_USERNAME)
                .password("hashed_pass")
                .isActive(true)
                .firstName("Active")
                .lastName("Trainee")
                .build();

        inactiveTrainer = Trainer.builder()
                .id(2L)
                .username(TRAINER_USERNAME)
                .password("hashed_pass")
                .isActive(false)
                .firstName("Inactive")
                .lastName("Trainer")
                .trainingType(mock(com.gymapp.model.TrainingType.class))
                .build();
    }

    @Test
    void loadUserByUsername_shouldLoadUserFromFirstProviderThatFindsIt() {
        when(traineeProvider.findUser(TRAINEE_USERNAME)).thenReturn(Optional.of(activeTrainee));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(TRAINEE_USERNAME);

        assertNotNull(userDetails);
        assertEquals(TRAINEE_USERNAME, userDetails.getUsername());
        assertEquals("hashed_pass", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());

        Optional<? extends GrantedAuthority> authority = userDetails.getAuthorities().stream().findFirst();
        assertTrue(authority.isPresent());
        assertEquals("ROLE_TRAINEE", authority.get().getAuthority());

        verify(traineeProvider).findUser(TRAINEE_USERNAME);
        verify(trainerProvider, never()).findUser(anyString());
    }

    @Test
    void loadUserByUsername_shouldIterateToNextProviderIfFirstReturnsEmpty() {
        when(traineeProvider.findUser(TRAINER_USERNAME)).thenReturn(Optional.empty());
        when(trainerProvider.findUser(TRAINER_USERNAME)).thenReturn(Optional.of(inactiveTrainer));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(TRAINER_USERNAME);

        assertNotNull(userDetails);
        assertEquals(TRAINER_USERNAME, userDetails.getUsername());
        assertFalse(userDetails.isEnabled());

        verify(traineeProvider).findUser(TRAINER_USERNAME);
        verify(trainerProvider).findUser(TRAINER_USERNAME);
    }

    @Test
    void loadUserByUsername_shouldThrowUsernameNotFoundExceptionWhenUserNotFound() {
        when(traineeProvider.findUser(UNKNOWN_USERNAME)).thenReturn(Optional.empty());
        when(trainerProvider.findUser(UNKNOWN_USERNAME)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(UNKNOWN_USERNAME));

        verify(traineeProvider).findUser(UNKNOWN_USERNAME);
        verify(trainerProvider).findUser(UNKNOWN_USERNAME);
    }

    @Test
    void loadUserByUsername_shouldMapInactiveUserCorrectly() {
        when(traineeProvider.findUser(TRAINER_USERNAME)).thenReturn(Optional.empty());
        when(trainerProvider.findUser(TRAINER_USERNAME)).thenReturn(Optional.of(inactiveTrainer));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(TRAINER_USERNAME);

        assertFalse(userDetails.isEnabled());
        assertEquals("ROLE_TRAINER", userDetails.getAuthorities().stream().findFirst().get().getAuthority());
    }

    @Test
    void loadUserByUsername_shouldMapActiveUserCorrectly() {
        when(traineeProvider.findUser(TRAINEE_USERNAME)).thenReturn(Optional.of(activeTrainee));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(TRAINEE_USERNAME);

        assertTrue(userDetails.isEnabled());
        assertEquals("ROLE_TRAINEE", userDetails.getAuthorities().stream().findFirst().get().getAuthority());
    }

    @Test
    void loadUserByUsername_shouldHandleEmptyProviderList() {
        CustomUserDetailsService emptyService = new CustomUserDetailsService(Collections.emptyList());

        assertThrows(UsernameNotFoundException.class,
                () -> emptyService.loadUserByUsername(UNKNOWN_USERNAME));
    }
}