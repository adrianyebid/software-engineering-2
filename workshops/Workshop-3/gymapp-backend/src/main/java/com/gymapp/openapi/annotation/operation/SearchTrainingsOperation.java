package com.gymapp.openapi.annotation.operation;

import com.gymapp.openapi.annotation.composed.SearchTrainingsResponses;
import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation
@SearchTrainingsResponses
public @interface SearchTrainingsOperation {
    String summary() default "Search trainings by filters";
    String description() default "";
}