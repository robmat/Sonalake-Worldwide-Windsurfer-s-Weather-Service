package edu.batodev.windsurf.controller;

import edu.batodev.windsurf.dto.BestLocationResponse;
import edu.batodev.windsurf.service.BestLocationService;
import edu.batodev.windsurf.validation.ValidForecastDate;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * REST controller for handling weather-related requests.
 */
@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final BestLocationService bestLocationService;

    /**
     * Finds and returns the best windsurfing location for a given date.
     * The endpoint is secured and requires 'SCOPE_weather.read' authority.
     *
     * @param date The date for which to find the best location, in yyyy-MM-dd format.
     *             The date must be today or in the future and within the valid forecast range.
     * @return A {@link ResponseEntity} containing the {@link BestLocationResponse} with a 200 OK status if a suitable location is found,
     *         or a 404 Not Found status if no suitable location is found.
     */
    @PreAuthorize("hasAuthority('SCOPE_weather.read')")
    @GetMapping("/best-location/{date}")
    public ResponseEntity<BestLocationResponse> getBestLocation(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @FutureOrPresent(message = "Date must be today or in the future")
            @ValidForecastDate
            LocalDate date) {
        return bestLocationService.findBestLocation(date)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
