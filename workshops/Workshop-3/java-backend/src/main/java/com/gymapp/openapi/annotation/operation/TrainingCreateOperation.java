package com.gymapp.openapi.annotation.operation;

import com.gymapp.openapi.annotation.composed.TrainingCreateResponses;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation
@TrainingCreateResponses
public @interface TrainingCreateOperation {

    @AliasFor(annotation = Operation.class, attribute = "summary")
    String summary() default "Create training";
    @AliasFor(annotation = Operation.class, attribute = "description")
    String description() default "Creates a new training session for a trainee";
}
