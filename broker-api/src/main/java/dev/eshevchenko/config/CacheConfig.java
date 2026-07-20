package dev.eshevchenko.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfig {

  @Value("${app.cache.clients.cache-name}")
  private String cacheName;

  @Value("${app.cache.clients.expire-after-write}")
  private int expireAfterWrite;

  @Value("${app.cache.clients.maximum-size}")
  private int maximumSize;

  @Bean
  public CacheManager cacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager(cacheName);
    cacheManager.setCaffeine(Caffeine.newBuilder()
      .expireAfterWrite(Duration.ofMinutes(expireAfterWrite))
      .maximumSize(maximumSize));
    return cacheManager;
  }
}