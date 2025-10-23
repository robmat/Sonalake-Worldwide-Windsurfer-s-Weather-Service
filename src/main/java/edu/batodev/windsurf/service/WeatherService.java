package edu.batodev.windsurf.service;

import edu.batodev.windsurf.config.CacheConfig;
import edu.batodev.windsurf.config.WeatherbitConfigProperties;
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

/**
 * Service responsible for fetching weather data from the external Weatherbit API.
 */
@Service
@RequiredArgsConstructor
public class WeatherService {
    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);

    private final RestTemplate restTemplate;
    private final WeatherbitConfigProperties weatherbitConfigProperties;

    /**
     * Fetches weather forecast data for a specific location and date.
     * This method is cacheable, so repeated calls with the same arguments within a short period
     * will return a cached result. It is also protected by a circuit breaker to handle
     * failures when calling the external API.
     *
     * @param location The {@link Location} for which to fetch the weather.
     * @param date The date of the forecast.
     * @return A {@link WeatherbitData} object containing the weather information for the specified date,
     *         or {@code null} if no data is available for that date or if an error occurs.
     */
    @Cacheable(value = CacheConfig.WEATHER_CACHE, key = "#location.id + '-' + #date")
    @CircuitBreaker(name = "weatherbit", fallbackMethod = "getWeatherFallback")
    public WeatherbitData getWeather(Location location, LocalDate date) {
        log.debug("Fetching weather data from API for location: {}, date: {}", location.getName(), date);
        String url = UriComponentsBuilder.fromUriString(weatherbitConfigProperties.getApi().getUrl())
            .queryParam("lat", location.getLatitude())
            .queryParam("lon", location.getLongitude())
            .queryParam("key", weatherbitConfigProperties.getApi().getKey())
            .toUriString();
        log.debug("Weatherbit API request URL: {}", url);
        WeatherbitResponse response = restTemplate.getForObject(url, WeatherbitResponse.class);
        if (response == null) {
            log.warn("Weatherbit API response is null for location: {}, date: {}", location.getName(), date);
            return null;
        }
        if (response.data() == null || response.data().isEmpty()) {
            log.warn("Weatherbit API returned empty data for location: {}, date: {}", location.getName(), date);
            return null;
        }
        for (WeatherbitData data : response.data()) {
            log.debug("Checking WeatherbitData: {}", data);
            if (date.equals(data.datetime())) {
                log.debug("Found weather data for date: {}: {}", date, data);
                return data;
            }
        }
        log.warn("No weather data found for date: {} in Weatherbit API response for location: {}", date, location.getName());
        return null;
    }

    /**
     * Fallback method for the circuit breaker on {@link #getWeather(Location, LocalDate)}.
     * This method is invoked if the external Weatherbit API call fails. It logs the error
     * and returns null.
     *
     * @param location The location for which the weather fetch failed.
     * @param date The date for which the weather fetch failed.
     * @param e The exception that caused the circuit breaker to open.
     * @return Always returns {@code null}.
     */
    @SuppressWarnings("unused") // Used by Resilience4j as a fallback method
    private WeatherbitData getWeatherFallback(Location location, LocalDate date, Exception e) {
        log.error("Circuit breaker fallback triggered for location: {}, date: {}. Error: {}",
                  location.getName(), date, e.getMessage());
        return null;
    }
}
