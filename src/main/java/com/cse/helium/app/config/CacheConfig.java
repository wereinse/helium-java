package com.cse.helium.app.config;

//import org.springframework.cache.CacheManager;
import java.time.Duration;
//import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ehcache.CacheManager;
//import org.ehcache.Cache;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Caching configuration class. */
@Configuration
@EnableCaching
public class CacheConfig {
  private final Logger logger = LogManager.getLogger(CacheConfig.class);

  /** Provide CacheManager for application. */
  @Bean
  public CacheManager cacheManager() {
    
    CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder() 
        .withCache("responseCache",
        CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, 
            ResourcePoolsBuilder.heap(10))
              .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(60))))
        .build(); 
    cacheManager.init();
    logger.info("CacheManager created and initialized");

    return cacheManager;
  }
}