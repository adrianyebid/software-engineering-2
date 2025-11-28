package com.gymapp.openapi.annotation.operation;

import com.gymapp.openapi.annotation.composed.AssignResponses;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation
@AssignResponses
public @interface AssignOperation {

    @AliasFor(annotation = Operation.class, attribute = "summary")
    String summary() default "Assign trainers to trainee";
    @AliasFor(annotation = Operation.class, attribute = "description")
    String description() default "";
}