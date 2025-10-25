package edu.batodev.windsurf.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Configuration for application caching.
 * Enables caching and configures the cache manager.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    public static final String WEATHER_CACHE = "weatherCache";

    /**
     * Configures the cache manager for the application.
     * Uses Caffeine as the cache implementation with a 30-second write expiration
     * and a maximum size of 100 entries.
     *
     * @return The configured {@link CacheManager}.
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(WEATHER_CACHE);
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(30, TimeUnit.SECONDS)
                .maximumSize(100));
        return cacheManager;
    }
}

