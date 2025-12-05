package com.gymapp.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gymapp.exception.ErrorResponse;
import com.gymapp.service.security.CustomUserDetailsService;
import com.gymapp.service.security.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtAuthenticationFilter Unit Tests")
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;
    @Mock
    private CustomUserDetailsService userDetailsService;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private PrintWriter printWriter;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String VALID_TOKEN = "Bearer valid.jwt.token";
    private static final String INVALID_TOKEN = "Bearer invalid.jwt.token";
    private static final String USERNAME = "testuser";
    private static final String REQUEST_URI = "/api/v1/resource";
    private UserDetails userDetails;

    @BeforeEach
    void setUp() { // Eliminamos throws IOException para simplificar
        userDetails = User.builder()
                .username(USERNAME)
                .password("pass")
                .authorities(Collections.emptyList())
                .build();

        // Limpiar el SecurityContext antes de cada prueba
        SecurityContextHolder.clearContext();

        // NO INCLUIMOS MOCKS ESPECÍFICOS DE LA RESPUESTA AQUÍ.
    }

    // ============================================================
    // PASS-THROUGH (No Auth/Already Auth) Tests
    // ============================================================

    @Test
    void doFilterInternal_shouldPassThrough_whenNoAuthorizationHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).extractUsername(anyString());
    }

    @Test
    void doFilterInternal_shouldPassThrough_whenHeaderDoesNotStartWithBearer() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Basic 12345");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).extractUsername(anyString());
    }

    @Test
    void doFilterInternal_shouldPassThrough_whenUserIsAlreadyAuthenticated() throws ServletException, IOException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(mock(UsernamePasswordAuthenticationToken.class));
        SecurityContextHolder.setContext(context);

        when(request.getHeader("Authorization")).thenReturn(VALID_TOKEN);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).extractUsername(anyString());
    }

    // ============================================================
    // Successful Authentication Tests
    // ============================================================

    @Test
    void doFilterInternal_shouldAuthenticateUser_whenTokenIsValid() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(VALID_TOKEN);
        when(jwtService.extractUsername(VALID_TOKEN.substring(7))).thenReturn(USERNAME);
        when(userDetailsService.loadUserByUsername(USERNAME)).thenReturn(userDetails);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService).extractUsername(VALID_TOKEN.substring(7));
        verify(userDetailsService).loadUserByUsername(USERNAME);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(USERNAME, SecurityContextHolder.getContext().getAuthentication().getName());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldPassThrough_whenTokenIsValidButUsernameIsNull() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(VALID_TOKEN);
        when(jwtService.extractUsername(VALID_TOKEN.substring(7))).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService).extractUsername(VALID_TOKEN.substring(7));
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    // ============================================================
    // JwtException Handling Tests
    // ============================================================

    @Test
    void doFilterInternal_shouldHandleJwtException_andSend401Response() throws ServletException, IOException {
        // Mocks necesarios SOLAMENTE para la excepción
        when(response.getWriter()).thenReturn(printWriter);
        when(request.getRequestURI()).thenReturn(REQUEST_URI);

        JwtException mockException = new JwtException("Token expired");
        when(request.getHeader("Authorization")).thenReturn(INVALID_TOKEN);
        when(jwtService.extractUsername(INVALID_TOKEN.substring(7))).thenThrow(mockException);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verifica que se haya detenido la cadena de filtros
        verify(filterChain, never()).doFilter(any(), any());

        // Verifica la configuración de la respuesta HTTP 401
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(response).setCharacterEncoding("UTF-8");

        // Captura el objeto ErrorResponse serializado
        ArgumentCaptor<ErrorResponse> errorResponseCaptor = ArgumentCaptor.forClass(ErrorResponse.class);
        verify(objectMapper).writeValue(eq(printWriter), errorResponseCaptor.capture());

        ErrorResponse capturedResponse = errorResponseCaptor.getValue();
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, capturedResponse.status());
        assertTrue(capturedResponse.message().contains("Token expired"));
        assertEquals(REQUEST_URI, capturedResponse.path());

        // El SecurityContext debe permanecer vacío (no autenticado)
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_shouldReturnImmediatelyAfterHandlingJwtException() throws ServletException, IOException {
        // Mocks necesarios SOLAMENTE para la excepción
        when(response.getWriter()).thenReturn(printWriter);
        when(request.getRequestURI()).thenReturn(REQUEST_URI);

        when(request.getHeader("Authorization")).thenReturn(INVALID_TOKEN);
        when(jwtService.extractUsername(INVALID_TOKEN.substring(7))).thenThrow(new JwtException("Invalid token"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Debe retornar inmediatamente después de manejar la excepción
        verify(filterChain, never()).doFilter(request, response);
    }
}