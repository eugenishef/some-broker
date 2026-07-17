package dev.eshevchenko.config;

import java.util.concurrent.Executor;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.support.ContextPropagatingTaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@Configuration
@EnableAsync
@EnableConfigurationProperties(ReportingProperties.class)
@RequiredArgsConstructor
public class ReportingAsyncConfig implements AsyncConfigurer {

  private final ReportingProperties properties;

  @Bean(name = "reportExecutor")
  public ThreadPoolTaskExecutor reportExecutor() {
    return buildExecutor(
      properties.getExecutor().getReport(),
      "report-exec-"
    );
  }

  @Bean(name = "reconciliationExecutor")
  public ThreadPoolTaskExecutor reconciliationExecutor() {
    return buildExecutor(
      properties.getExecutor().getReconciliation(),
      "reconciliation-exec-"
    );
  }

  private ThreadPoolTaskExecutor buildExecutor(ReportingProperties.PoolConfig config, String prefix) {

    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

    executor.setCorePoolSize(config.getCoreSize());
    executor.setMaxPoolSize(config.getMaxSize());
    executor.setQueueCapacity(config.getQueueCapacity());

    executor.setThreadNamePrefix(prefix);

    executor.setTaskDecorator(
      new CompositeTaskDecorator(
        List.of(
          new MdcTaskDecorator(),
          new ContextPropagatingTaskDecorator()
        )
      )
    );

    executor.setRejectedExecutionHandler(
      new ThreadPoolExecutor.CallerRunsPolicy()
    );

    executor.setWaitForTasksToCompleteOnShutdown(true);
    executor.setAwaitTerminationSeconds(30);

    executor.initialize();

    return executor;
  }


  @Override
  public Executor getAsyncExecutor() {
    return reportExecutor();
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {

    return (throwable, method, params) ->
      log.error(
        "Ошибка в async методе {}",
        method.getName(),
        throwable
      );
  }
}