package com.gymapp.config;

import com.gymapp.utils.PathsConstants;
import com.gymapp.interceptor.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos de autenticación
                        .requestMatchers(HttpMethod.POST, PathsConstants.API.Auth.LOGIN).permitAll()
                        .requestMatchers(HttpMethod.POST, PathsConstants.API.Auth.REFRESH).permitAll()
                        .requestMatchers(HttpMethod.POST, PathsConstants.API.Auth.LOGOUT).permitAll()
                        .requestMatchers(HttpMethod.POST, PathsConstants.API.TRAINEES).permitAll()
                        .requestMatchers(HttpMethod.POST, PathsConstants.API.TRAINERS).permitAll()

                        // Endpoints públicos de Swagger/OpenAPI
                        .requestMatchers(PathsConstants.Docs.SWAGGER_UI).permitAll()
                        .requestMatchers(PathsConstants.Docs.API_DOCS).permitAll()
                        .requestMatchers(PathsConstants.Docs.SWAGGER_UI_HTML).permitAll()

                        .anyRequest().authenticated()
                );
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(PathsConstants.Frontend.LOCAL));
        config.setAllowedMethods(Arrays.asList(PathsConstants.Methods.getAllMethodsArray()));
        config.setAllowedHeaders(Arrays.asList(PathsConstants.Headers.getCorsAllowedHeadersArray()));
        config.setAllowCredentials(PathsConstants.Cors.ALLOW_CREDENTIALS);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(PathsConstants.Cors.ALL_PATHS, config);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}