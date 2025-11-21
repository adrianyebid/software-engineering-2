package com.gymapp.openapi.annotation.operation;

import com.gymapp.openapi.annotation.composed.DeleteResponses;
import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation
@DeleteResponses
public @interface DeleteOperation {
    String summary() default "Delete new resource";

    String description() default "";
}