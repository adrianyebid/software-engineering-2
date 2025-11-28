package com.gymapp.openapi.annotation.base.response.client_error;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(ResponseTooManyRequests_429.Container.class)
@ApiResponse(
        responseCode = "429",
        description = "User temporarily locked due to too many failed attempts"
)
public @interface ResponseTooManyRequests_429 {
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Container {
        ResponseTooManyRequests_429[] value();
    }
}
