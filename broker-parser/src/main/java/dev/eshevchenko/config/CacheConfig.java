package dev.eshevchenko.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfig {

  @Value("${app.cache.reports.cache-name}")
  private String reportsCacheName;

  @Value("${app.cache.reports.expire-after-write}")
  private Duration reportsExpireAfterWrite;

  @Value("${app.cache.reports.maximum-size}")
  private long reportsMaximumSize;

  @Value("${app.cache.report-versions.cache-name}")
  private String reportVersionsCacheName;

  @Value("${app.cache.report-versions.expire-after-write}")
  private Duration reportVersionsExpireAfterWrite;

  @Value("${app.cache.report-versions.maximum-size}")
  private long reportVersionsMaximumSize;

  @Bean
  public CacheManager cacheManager() {

    Cache reportsCache = new CaffeineCache(
      reportsCacheName,
      Caffeine.newBuilder()
        .expireAfterWrite(reportsExpireAfterWrite)
        .maximumSize(reportsMaximumSize)
        .build()
    );

    Cache reportVersionsCache = new CaffeineCache(
      reportVersionsCacheName,
      Caffeine.newBuilder()
        .expireAfterWrite(reportVersionsExpireAfterWrite)
        .maximumSize(reportVersionsMaximumSize)
        .build()
    );

    SimpleCacheManager cacheManager = new SimpleCacheManager();
    cacheManager.setCaches(List.of(reportsCache, reportVersionsCache));

    return cacheManager;
  }
}