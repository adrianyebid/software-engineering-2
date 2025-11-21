package com.gymapp.openapi.annotation.operation;

import com.gymapp.openapi.annotation.composed.AssignResponses;
import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation
@AssignResponses
public @interface AssignOperation {
    String summary() default "Assign trainers to trainee";
    String description() default "";
}