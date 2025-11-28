package com.gymapp.openapi.annotation.base.response.client_error;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(ResponseForbidden_403.Container.class)
@ApiResponse(
        responseCode = "403",
        description = "Access forbidden"
)
public @interface ResponseForbidden_403 {
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Container {
        ResponseForbidden_403[] value();
    }
}