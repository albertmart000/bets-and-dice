package com.betsanddice.craps.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Betsanddice-craps microservice REST API Documentation", version = "1.0", description = "REST API in bets-and-dice to for Craps Game"))
public class OpenApiConfig {
}
