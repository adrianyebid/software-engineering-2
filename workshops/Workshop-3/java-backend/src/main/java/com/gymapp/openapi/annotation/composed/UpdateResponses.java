package com.gymapp.openapi.annotation.composed;

import com.gymapp.openapi.annotation.base.response.client_error.ResponseBadRequest_400;
import com.gymapp.openapi.annotation.base.response.client_error.ResponseForbidden_403;
import com.gymapp.openapi.annotation.base.response.client_error.ResponseNotFound_404;
import com.gymapp.openapi.annotation.base.response.server_error.ResponseInternalServerError_500;
import com.gymapp.openapi.annotation.base.response.successful.ResponseOK_200;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ResponseOK_200
@ResponseBadRequest_400
@ResponseForbidden_403
@ResponseNotFound_404
@ResponseInternalServerError_500
public @interface UpdateResponses {
}