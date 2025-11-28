package com.gymapp.openapi.annotation.base.response.client_error;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(ResponseUnauthorized_401.Container.class)
@ApiResponse(
        responseCode = "401",
        description = "Unauthorized access"
)
public @interface ResponseUnauthorized_401 {
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Container {
        ResponseUnauthorized_401[] value();
    }
}