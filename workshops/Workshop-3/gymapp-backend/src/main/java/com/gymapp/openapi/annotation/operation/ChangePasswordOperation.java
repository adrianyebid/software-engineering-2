package com.gymapp.openapi.annotation.operation;

import com.gymapp.openapi.annotation.composed.ChangePasswordResponses;
import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation
@ChangePasswordResponses
public @interface ChangePasswordOperation {
    String summary() default "Change user password";
    String description() default "Updates the user's password after validating current credentials";
}