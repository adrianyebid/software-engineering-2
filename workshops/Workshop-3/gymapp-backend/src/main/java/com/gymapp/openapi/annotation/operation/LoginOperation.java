package com.gymapp.openapi.annotation.operation;

import com.gymapp.openapi.annotation.composed.LoginResponses;
import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation
@LoginResponses
public @interface LoginOperation {
    String summary() default "Authenticate user";
    String description() default "Validates username and password credentials";
}