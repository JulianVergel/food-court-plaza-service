package com.foodcourt.plaza_service.infrastructure.documentation;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Plazoleta Microservice API",
                version = "1.0.0",
                description = "Esta API proporciona los endpoints para administrar los restaurantes, platos y pedidos de la Plazoleta de Comidas."
        )
)
public class OpenApiConfiguration {
}
