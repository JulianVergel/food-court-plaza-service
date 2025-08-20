package com.foodcourt.plaza_service.infrastructure.input.doc;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.foodcourt.plaza_service.infrastructure.input.doc.SwaggerConstants.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(responseCode = "403", description = RESPONSE_403_DESCRIPTION, content = @Content),
        @ApiResponse(responseCode = "404", description = RESPONSE_404_DESCRIPTION, content = @Content),
        @ApiResponse(responseCode = "409", description = RESPONSE_409_DESCRIPTION, content = @Content)
})
public @interface StandardApiResponses {
}
