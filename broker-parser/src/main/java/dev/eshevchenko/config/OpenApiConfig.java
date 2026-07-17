package dev.eshevchenko.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
  info = @Info(
    title = "Broker API",
    version = "1.0",
    description = "Сервис для загрузки отчетов из внешних файлов"
  )
)
public class OpenApiConfig {
}