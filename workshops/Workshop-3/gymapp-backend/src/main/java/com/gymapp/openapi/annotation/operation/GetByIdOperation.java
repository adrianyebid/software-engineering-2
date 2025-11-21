package com.gymapp.openapi.annotation.operation;

import com.gymapp.openapi.annotation.composed.GetByIdResponses;
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
@GetByIdResponses
public @interface GetByIdOperation {
    String summary() default "Get resource by Id";

    String description() default "";
}