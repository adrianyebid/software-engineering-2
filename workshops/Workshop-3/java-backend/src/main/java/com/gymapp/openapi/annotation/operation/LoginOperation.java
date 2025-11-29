package com.gymapp.openapi.annotation.operation;

import com.gymapp.openapi.annotation.composed.LoginResponses;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation
@LoginResponses
public @interface LoginOperation {
    @AliasFor(annotation = Operation.class, attribute = "summary")
    String summary() default "Authenticate user";

    @AliasFor(annotation = Operation.class, attribute = "description")
    String description() default "Validates username and password credentials";
}