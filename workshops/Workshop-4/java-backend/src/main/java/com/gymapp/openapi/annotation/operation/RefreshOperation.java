package com.gymapp.openapi.annotation.operation;

import com.gymapp.openapi.annotation.composed.RefreshResponses;
import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation
@RefreshResponses
public @interface RefreshOperation {
    String summary() default "Refresh access token";
    String description() default "Generates a new access token using a valid refresh token";
}
