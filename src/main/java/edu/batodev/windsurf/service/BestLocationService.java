package edu.batodev.windsurf.service;

import edu.batodev.windsurf.config.ConcurrencyConfig;
import edu.batodev.windsurf.dto.BestLocationResponse;
import edu.batodev.windsurf.dto.WeatherbitData;
import edu.batodev.windsurf.model.Location;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Service responsible for determining the best windsurfing location.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class BestLocationService {

    private final LocationService locationService;
    private final WeatherService weatherService;
    private final ConcurrencyConfig.ExecutorServiceFactory executorServiceFactory;

    /**
     * Finds the best windsurfing location among a list of predefined locations for a specific date.
     * It fetches weather data for each location, calculates a score based on wind speed and temperature,
     * and returns the location with the highest score that meets the suitability criteria.
     *
     * @param date The date for which to find the best location.
     * @return An {@link Optional} containing the {@link BestLocationResponse} for the best location,
     *         or an empty Optional if no locations are suitable for windsurfing on the given date.
     */
    public Optional<BestLocationResponse> findBestLocation(LocalDate date) {
        List<Location> locations = locationService.getAllLocations();
        log.debug("Evaluating best location for date: {}. Locations: {}", date, locations);

        try (ExecutorService executor = executorServiceFactory.getObject()) {
            List<Future<BestLocationResponse>> futures = locations.stream()
                    .map(location -> executor.submit(() -> {
                        log.debug("Checking location: {}", location.getName());
                        WeatherbitData weather = weatherService.getWeather(location, date);
                        if (weather == null) {
                            log.warn("No weather data for location: {} on date: {}", location.getName(), date);
                            return null;
                        }
                        log.debug("Weather data for location {}: temp={}, wind_spd={}", location.getName(), weather.temp(), weather.wind_spd());
                        BigDecimal score = calculateScore(weather.temp(), weather.wind_spd());
                        log.debug("Calculated score for location {}: {}", location.getName(), score);
                        BestLocationResponse response = new BestLocationResponse(location.getName(), weather.temp(), weather.wind_spd(), score);
                        if (!isSuitableForWindsurfing(response)) {
                            log.debug("Location {} is not suitable for windsurfing on date {}", location.getName(), date);
                        }
                        return response;
                    }))
                    .toList();

            return futures.stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            throw new WeatherDataFetchInterruptedException("Thread interrupted while fetching weather data", e);
                        } catch (ExecutionException e) {
                            log.error("Error fetching weather data", e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .filter(this::isSuitableForWindsurfing)
                    .max(Comparator.comparing(BestLocationResponse::score));
        }
    }


    /**
     * Checks if a location's weather conditions are suitable for windsurfing.
     * Suitability is defined by wind speed between 5 and 18 m/s (inclusive) and
     * temperature between 5 and 35 degrees Celsius (inclusive).
     *
     * @param location The location's weather data encapsulated in a {@link BestLocationResponse}.
     * @return {@code true} if the location is suitable for windsurfing, {@code false} otherwise.
     */
     boolean isSuitableForWindsurfing(BestLocationResponse location) {
        return location.windSpeed().compareTo(BigDecimal.valueOf(5)) >= 0 &&
                location.windSpeed().compareTo(BigDecimal.valueOf(18)) <= 0 &&
                location.temperature().compareTo(BigDecimal.valueOf(5)) >= 0 &&
                location.temperature().compareTo(BigDecimal.valueOf(35)) <= 0;
    }

    /**
     * Calculates the windsurfing score for a location based on its weather conditions.
     * The formula is: (wind speed * 3) + temperature.
     *
     * @param temp The average temperature in Celsius.
     * @param windSpd The wind speed in m/s.
     * @return The calculated score as a {@link BigDecimal}.
     */
    private BigDecimal calculateScore(BigDecimal temp, BigDecimal windSpd) {
        return BigDecimal.valueOf(windSpd.doubleValue() * 3 + temp.doubleValue());
    }

    /**
     * Custom exception to indicate that weather data fetching was interrupted.
     */
    static class WeatherDataFetchInterruptedException extends RuntimeException {
        public WeatherDataFetchInterruptedException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
