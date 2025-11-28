package com.gymapp.openapi.annotation.base.response.server_error;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(ResponseInternalServerError_500.Container.class)
@ApiResponse(
        responseCode = "500",
        description = "Internal Server Error"
)
public @interface ResponseInternalServerError_500 {
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Container {
        ResponseInternalServerError_500[] value();
    }
}