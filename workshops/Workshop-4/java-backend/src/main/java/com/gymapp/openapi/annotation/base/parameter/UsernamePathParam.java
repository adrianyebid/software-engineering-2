package com.gymapp.openapi.annotation.base.parameter;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Parameter(
        description = "Trainee unique username",
        required = true,
        example = "Maelo.Ruiz"
)
public @interface UsernamePathParam {
    String value() default "username";

    String example() default "john.doe";
}
