package com.gymapp.openapi.annotation.operation;

import com.gymapp.openapi.annotation.composed.LogoutResponses;
import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.*;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation
@LogoutResponses
public @interface LogoutOperation {
    String summary() default "Logout user";
    String description() default "Revokes the provided refresh token";
}
