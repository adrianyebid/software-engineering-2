package com.gymapp.openapi.annotation.operation;

import com.gymapp.openapi.annotation.composed.SearchTrainingsResponses;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation
@SearchTrainingsResponses
public @interface SearchTrainingsOperation {

    @AliasFor(annotation = Operation.class, attribute = "summary")
    String summary() default "Search trainings by filters";
    @AliasFor(annotation = Operation.class, attribute = "description")
    String description() default "";
}