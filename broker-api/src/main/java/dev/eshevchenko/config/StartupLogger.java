package dev.eshevchenko.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartupLogger {

  @Value("${server.port:8080}")
  private int port;

  @Value("${server.address:localhost}")
  private String host;

  @Value("${server.servlet.context-path:}")
  private String contextPath;

  @Value("${springdoc.swagger-ui.path:/swagger-ui/index.html}")
  private String swaggerPath;

  @Value("${server.ssl.enabled:false}")
  private boolean sslEnabled;

  @EventListener(ApplicationReadyEvent.class)
  public void onReady() {
    String protocol = sslEnabled ? "https" : "http";

    log.info("Swagger UI: {}://{}:{}{}{}",
      protocol,
      host,
      port,
      contextPath,
      swaggerPath);
  }

}