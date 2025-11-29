package com.gymapp.openapi.annotation.operation;

import com.gymapp.openapi.annotation.composed.ChangePasswordResponses;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation
@ChangePasswordResponses
public @interface ChangePasswordOperation {
    @AliasFor(annotation = Operation.class, attribute = "summary")
    String summary() default "Change user password";

    @AliasFor(annotation = Operation.class, attribute = "description")
    String description() default "Updates the user's password after validating current credentials";
}