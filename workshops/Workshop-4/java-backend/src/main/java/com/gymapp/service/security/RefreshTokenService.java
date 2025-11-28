package com.gymapp.service.security;

import com.gymapp.model.RefreshToken;
import com.gymapp.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repo;

    public RefreshTokenService(RefreshTokenRepository repo) {
        this.repo = repo;
    }

    public RefreshToken createToken(String username) {
        RefreshToken rt = new RefreshToken();
        rt.setUsername(username);
        rt.setToken(UUID.randomUUID().toString());
        rt.setExpiryDate(Instant.now().plus(30, ChronoUnit.DAYS));
        rt.setRevoked(false);
        return repo.save(rt);
    }

    public boolean validateToken(String token) {
        return repo.findByToken(token)
                .filter(rt -> !rt.isRevoked() && rt.getExpiryDate().isAfter(Instant.now()))
                .isPresent();
    }

    public Optional<RefreshToken> findByToken(String token) {
        return repo.findByToken(token)
                .filter(rt -> !rt.isRevoked() && rt.getExpiryDate().isAfter(Instant.now()));
    }

    public void revokeToken(String token) {
        repo.findByToken(token).ifPresent(rt -> {
            rt.setRevoked(true);
            repo.save(rt);
        });
    }

    public void revokeAllForUser(String username) {
        repo.deleteByUsername(username);
    }
}
