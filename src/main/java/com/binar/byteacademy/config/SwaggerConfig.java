package com.binar.byteacademy.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;


@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)

@OpenAPIDefinition(
        info = @Info(
                title = "ByteAcademy",
                version = "v1.0.0",
                description = "Swagger UI for ByteAcademy"
        ),
        security = {
            @SecurityRequirement(
                    name = "bearerAuth"
            ),
        }

)
public class SwaggerConfig {
}