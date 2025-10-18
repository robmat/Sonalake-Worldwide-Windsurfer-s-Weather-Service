package edu.batodev.windsurf.service;

import edu.batodev.windsurf.config.CacheConfig;
import edu.batodev.windsurf.dto.WeatherbitData;
import edu.batodev.windsurf.dto.WeatherbitResponse;
import edu.batodev.windsurf.model.Location;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);

    private final RestTemplate restTemplate;

    @Value("${weatherbit.api.key}")
    private String apiKey;

    @Value("${weatherbit.api.url}")
    private String apiUrl;

    @Cacheable(value = CacheConfig.WEATHER_CACHE, key = "#location.id + '-' + #date")
    @CircuitBreaker(name = "weatherbit", fallbackMethod = "getWeatherFallback")
    public WeatherbitData getWeather(Location location, LocalDate date) {
        log.debug("Fetching weather data from API for location: {}, date: {}", location.getName(), date);
        String url = UriComponentsBuilder.fromUriString(apiUrl)
            .queryParam("lat", location.getLatitude())
            .queryParam("lon", location.getLongitude())
            .queryParam("key", apiKey)
            .toUriString();
        WeatherbitResponse response = restTemplate.getForObject(url, WeatherbitResponse.class);
        if (response != null && response.data() != null && !response.data().isEmpty()) {
            for (WeatherbitData data : response.data()) {
                if (date.equals(data.datetime())) {
                    return data;
                }
            }
            log.warn("No weather data found for date: {}", date);
        }
        return null;
    }

    @SuppressWarnings("unused") // Used by Resilience4j as a fallback method
    private WeatherbitData getWeatherFallback(Location location, LocalDate date, Exception e) {
        log.error("Circuit breaker fallback triggered for location: {}, date: {}. Error: {}",
                  location.getName(), date, e.getMessage());
        return null;
    }
}
