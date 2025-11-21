package com.gymapp.openapi.annotation.base.parameter;

import com.gymapp.dto.request.trainee.CreateTraineeDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@RequestBody(
        description = "Trainee updated data",
        required = true,
        content = @Content(schema = @Schema(implementation = CreateTraineeDTO.class))
)
public @interface traineeDtoRequestBody {
    String value() default "username";

    String example() default "Adrian.Rincon";
}
