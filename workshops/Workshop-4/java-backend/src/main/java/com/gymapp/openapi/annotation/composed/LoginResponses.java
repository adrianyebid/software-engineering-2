package com.gymapp.openapi.annotation.composed;

import com.gymapp.openapi.annotation.base.response.client_error.ResponseBadRequest_400;
import com.gymapp.openapi.annotation.base.response.client_error.ResponseTooManyRequests_429;
import com.gymapp.openapi.annotation.base.response.client_error.ResponseUnauthorized_401;
import com.gymapp.openapi.annotation.base.response.server_error.ResponseInternalServerError_500;
import com.gymapp.openapi.annotation.base.response.successful.ResponseOK_200;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ResponseOK_200
@ResponseBadRequest_400
@ResponseUnauthorized_401
@ResponseTooManyRequests_429
@ResponseInternalServerError_500
public @interface LoginResponses {
}