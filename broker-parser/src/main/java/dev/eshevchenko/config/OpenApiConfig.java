package dev.eshevchenko.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI openAPI(
    @Value("${app.api.version}") String apiVersion) {

    return new OpenAPI()
      .info(new io.swagger.v3.oas.models.info.Info()
        .title("Broker API")
        .version(apiVersion)
        .description("Сервис для загрузки отчетов из внешних файлов"));
  }
}