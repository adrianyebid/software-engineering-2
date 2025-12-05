package com.gymapp.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtService Unit Tests")
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private static final String TEST_SECRET = "thisisasecretkeyforjwtunit testingpurposes1234567890"; // 50 chars for HS256
    private static final long EXPIRATION_MINUTES = 60;
    private static final String TEST_USERNAME = "testuser";
    private Collection<SimpleGrantedAuthority> TEST_AUTHORITIES;

    @BeforeEach
    void setUp() {

        ReflectionTestUtils.setField(jwtService, "secret", TEST_SECRET);
        ReflectionTestUtils.setField(jwtService, "expirationMinutes", EXPIRATION_MINUTES);

        TEST_AUTHORITIES = List.of(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Test
    void generateToken_shouldGenerateValidToken() {
        String token = jwtService.generateToken(TEST_USERNAME, TEST_AUTHORITIES);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsername_shouldExtractCorrectUsernameFromValidToken() {
        String token = jwtService.generateToken(TEST_USERNAME, TEST_AUTHORITIES);
        String extractedUsername = jwtService.extractUsername(token);

        assertEquals(TEST_USERNAME, extractedUsername);
    }

    @Test
    void generateToken_shouldContainCorrectRolesClaim() {
        String token = jwtService.generateToken(TEST_USERNAME, TEST_AUTHORITIES);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(TEST_SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<String> roles = claims.get("roles", List.class);
        assertNotNull(roles);
        assertTrue(roles.contains("ROLE_USER"));
        assertTrue(roles.contains("ROLE_ADMIN"));
    }


    @Test
    void extractUsername_shouldThrowExceptionForInvalidSignature() {
        String token = jwtService.generateToken(TEST_USERNAME, TEST_AUTHORITIES);

        String WRONG_SECRET = "anothersecretkeythathasdifferentcontent0987654321";

        ReflectionTestUtils.setField(jwtService, "secret", WRONG_SECRET);

        assertThrows(SignatureException.class, () -> jwtService.extractUsername(token));
    }

    @Test
    void extractUsername_shouldThrowExceptionForMalformedToken() {
        String malformedToken = "header.payload.signature";
        assertThrows(io.jsonwebtoken.MalformedJwtException.class, () -> jwtService.extractUsername(malformedToken));
    }

}