package com.gymapp.openapi.annotation.operation;

import com.gymapp.openapi.annotation.composed.ChangeStatusResponses;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation
@ChangeStatusResponses
public @interface ChangeStatusOperation {

    @AliasFor(annotation = Operation.class, attribute = "summary")
    String summary() default "Change active status";
    @AliasFor(annotation = Operation.class, attribute = "description")
    String description() default "";
}
