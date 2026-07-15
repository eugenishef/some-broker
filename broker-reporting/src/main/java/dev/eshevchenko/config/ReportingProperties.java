package dev.eshevchenko.config;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.reporting")
@Getter
@Setter
public class ReportingProperties {

  private Kafka kafka = new Kafka();
  private Executor executor = new Executor();

  @Getter @Setter
  public static class Kafka {
    private Map<String, String> topics;
  }

  @Getter @Setter
  public static class Executor {
    private PoolConfig report = new PoolConfig();
    private PoolConfig reconciliation = new PoolConfig();
  }

  @Getter @Setter
  public static class PoolConfig {
    private int coreSize;
    private int maxSize;
    private int queueCapacity;
  }
}