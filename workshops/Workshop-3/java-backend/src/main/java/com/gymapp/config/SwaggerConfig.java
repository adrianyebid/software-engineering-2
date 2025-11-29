package com.gymapp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public static final String GYM_APP_API = "GymApp API";
    public static final String VERSION = "1.0";
    public static final String BEARER_AUTH = "bearerAuth";
    public static final String BEARER = "bearer";
    public static final String JWT = "JWT";

    @Bean
    public OpenAPI gymAppOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(GYM_APP_API)
                        .version(VERSION)
                        .description("API documentation for GymApp modules: trainees, trainers, trainings, and authentication"))
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme(BEARER)
                                .bearerFormat(JWT)
                                .description("JWT Bearer token authentication")));
    }
}