package edu.batodev.windsurf.service;

import edu.batodev.windsurf.dto.BestLocationResponse;
import edu.batodev.windsurf.dto.WeatherbitData;
import edu.batodev.windsurf.model.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BestLocationService {

    private final LocationService locationService;
    private final WeatherService weatherService;

    public Optional<BestLocationResponse> findBestLocation(LocalDate date) {
        List<Location> locations = locationService.getAllLocations();

        return locations.stream()
                .map(location -> {
                    WeatherbitData weather = weatherService.getWeather(location, date);
                    if (weather == null) {
                        return null;
                    }
                    BigDecimal score = calculateScore(weather.temp(), weather.wind_spd());
                    return new BestLocationResponse(location.getName(), weather.temp(), weather.wind_spd(), score);
                })
                .filter(Objects::nonNull)
                .filter(this::isSuitableForWindsurfing)
                .max(Comparator.comparing(BestLocationResponse::score));
    }


    private boolean isSuitableForWindsurfing(BestLocationResponse location) {
        return location.windSpeed().compareTo(BigDecimal.valueOf(5)) >= 0 &&
                location.windSpeed().compareTo(BigDecimal.valueOf(18)) <= 0 &&
                location.temperature().compareTo(BigDecimal.valueOf(5)) >= 0 &&
                location.temperature().compareTo(BigDecimal.valueOf(35)) <= 0;
    }

    private BigDecimal calculateScore(BigDecimal temp, BigDecimal windSpd) {
        return BigDecimal.valueOf(windSpd.doubleValue() * 3 + temp.doubleValue());
    }
}
