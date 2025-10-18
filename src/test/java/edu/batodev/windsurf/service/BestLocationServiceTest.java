package edu.batodev.windsurf.service;

import edu.batodev.windsurf.dto.BestLocationResponse;
import edu.batodev.windsurf.dto.WeatherbitData;
import edu.batodev.windsurf.model.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BestLocationServiceTest {

    @Mock
    private LocationService locationService;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private BestLocationService bestLocationService;

    @Test
    void findBestLocation_shouldReturnBestLocation_whenMultipleLocationsAreSuitable() {
        // Given
        LocalDate date = LocalDate.now();
        Location jastarnia = new Location(1L, "Jastarnia", new BigDecimal("54.70"), new BigDecimal("18.68"));
        Location bridgetown = new Location(2L, "Bridgetown", new BigDecimal("13.10"), new BigDecimal("-59.62"));

        when(locationService.getAllLocations()).thenReturn(List.of(jastarnia, bridgetown));
        when(weatherService.getWeather(jastarnia, date)).thenReturn(new WeatherbitData(new BigDecimal("10"), new BigDecimal("10"), date));
        when(weatherService.getWeather(bridgetown, date)).thenReturn(new WeatherbitData(new BigDecimal("20"), new BigDecimal("15"), date));

        // When
        Optional<BestLocationResponse> bestLocation = bestLocationService.findBestLocation(date);

        // Then
        assertThat(bestLocation).isPresent();
        assertThat(bestLocation.get().location()).isEqualTo("Bridgetown");
        assertThat(bestLocation.get().temperature()).isEqualByComparingTo(new BigDecimal("20"));
        assertThat(bestLocation.get().windSpeed()).isEqualByComparingTo(new BigDecimal("15"));
    }

    @Test
    void findBestLocation_shouldReturnEmpty_whenNoLocationsAreSuitable() {
        // Given
        LocalDate date = LocalDate.now();
        Location jastarnia = new Location(1L, "Jastarnia", new BigDecimal("54.70"), new BigDecimal("18.68"));

        when(locationService.getAllLocations()).thenReturn(List.of(jastarnia));
        when(weatherService.getWeather(jastarnia, date)).thenReturn(new WeatherbitData(new BigDecimal("0"), new BigDecimal("0"), date));

        // When
        Optional<BestLocationResponse> bestLocation = bestLocationService.findBestLocation(date);

        // Then
        assertThat(bestLocation).isNotPresent();
    }
}
