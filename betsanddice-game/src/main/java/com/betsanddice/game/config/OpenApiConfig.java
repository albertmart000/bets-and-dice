package com.betsanddice.game.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Bets And Dice", version = "1.0", description = "Documentation API"))
public class OpenApiConfig {
}
