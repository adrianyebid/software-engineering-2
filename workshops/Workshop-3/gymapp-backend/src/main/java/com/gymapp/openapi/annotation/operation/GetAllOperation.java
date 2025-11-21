package com.gymapp.openapi.annotation.operation;

import com.gymapp.openapi.annotation.composed.GetAllResponses;
import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation
@GetAllResponses
public @interface GetAllOperation {
    String summary() default "Get all resources";

    String description() default "";
}