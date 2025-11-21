package com.gymapp.openapi.annotation.operation;

import com.gymapp.openapi.annotation.composed.CreateVoidResponses;
import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation
@CreateVoidResponses
public @interface CreateVoidOperation {
    String summary() default "Create training";
    String description() default "";
}