package com.gymapp.openapi.annotation.base.response.client_error;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(ResponseBadRequest_400.Container.class)
@ApiResponse(
        responseCode = "400",
        description = "Invalid data in the request"
)
public @interface ResponseBadRequest_400 {
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Container {
        ResponseBadRequest_400[] value();
    }
}