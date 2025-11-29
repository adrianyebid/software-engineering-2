package com.gymapp.openapi.annotation.composed;

import com.gymapp.openapi.annotation.base.response.client_error.ResponseBadRequest_400;
import com.gymapp.openapi.annotation.base.response.client_error.ResponseForbidden_403;
import com.gymapp.openapi.annotation.base.response.client_error.ResponseNotFound_404;
import com.gymapp.openapi.annotation.base.response.server_error.ResponseInternalServerError_500;
import com.gymapp.openapi.annotation.base.response.successful.ResponseNoContent_204;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ResponseNoContent_204
@ResponseBadRequest_400
@ResponseForbidden_403
@ResponseNotFound_404
@ResponseInternalServerError_500
public @interface ChangeStatusResponses {
}