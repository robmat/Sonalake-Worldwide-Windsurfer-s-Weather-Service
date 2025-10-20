package edu.batodev.windsurf.controller;

import edu.batodev.windsurf.dto.BestLocationResponse;
import edu.batodev.windsurf.service.BestLocationService;
import edu.batodev.windsurf.validation.ValidForecastDate;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final BestLocationService bestLocationService;

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
