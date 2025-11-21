package com.gymapp.openapi.annotation.operation;

import com.gymapp.openapi.annotation.composed.ChangeStatusResponses;
import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation
@ChangeStatusResponses
public @interface ChangeStatusOperation {
    String summary() default "Change active status";
    String description() default "";
}