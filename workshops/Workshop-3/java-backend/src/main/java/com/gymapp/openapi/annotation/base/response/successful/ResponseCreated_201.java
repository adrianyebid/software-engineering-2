package com.gymapp.openapi.annotation.base.response.successful;

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
@Repeatable(ResponseCreated_201.Container.class)
@ApiResponse(
        responseCode = "201",
        description = "Recurse created successfully"
)
public @interface ResponseCreated_201 {
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Container {
        ResponseCreated_201[] value();
    }
}
