package edu.batodev.windsurf.controller;

import edu.batodev.windsurf.advice.GlobalExceptionHandler;
import edu.batodev.windsurf.dto.BestLocationResponse;
import edu.batodev.windsurf.service.BestLocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class WeatherControllerTest {

    MockMvc mockMvc;

    @Mock
    BestLocationService bestLocationService;

    @InjectMocks
    WeatherController weatherController;

    LocalDate today = LocalDate.now();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(weatherController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getBestLocation_shouldReturnBestLocation_whenServiceReturnsData() throws Exception {
        BestLocationResponse response = new BestLocationResponse("Beach A", new BigDecimal("5.0"), new BigDecimal("5.0"), BigDecimal.ONE);
        when(bestLocationService.findBestLocation(today)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/weather/best-location/{date}", today.format(DateTimeFormatter.ISO_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value("Beach A"))
                .andExpect(jsonPath("$.score").value(1.0));
    }

    @Test
    void getBestLocation_shouldReturnNotFound_whenServiceReturnsEmpty() throws Exception {
        when(bestLocationService.findBestLocation(today)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/weather/best-location/{date}", today)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBestLocation_shouldReturnBadRequest_whenDateFormatIsInvalid() throws Exception {
        mockMvc.perform(get("/api/weather/best-location/{date}", "2025-10-200")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBestLocation_shouldReturnBadRequest_whenDateIsNotISOFormat() throws Exception {
        mockMvc.perform(get("/api/weather/best-location/{date}", "10/18/2025")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBestLocation_shouldReturnBadRequest_whenDateIsEmpty() throws Exception {
        mockMvc.perform(get("/api/weather/best-location/{date}", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Empty path variable results in 404
    }

    @Test
    void getBestLocation_shouldReturnBadRequest_whenDateContainsInvalidCharacters() throws Exception {
        mockMvc.perform(get("/api/weather/best-location/{date}", "2025-AB-18")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBestLocation_shouldAcceptValidISODate() throws Exception {
        LocalDate maxDate = LocalDate.now().plusDays(16);
        BestLocationResponse response = new BestLocationResponse("Beach A", new BigDecimal("5.0"), new BigDecimal("5.0"), BigDecimal.ONE);
        when(bestLocationService.findBestLocation(maxDate)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/weather/best-location/{date}", maxDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getBestLocation_shouldReturnBadRequest_whenDateIsInPast() throws Exception {
        mockMvc.perform(get("/api/weather/best-location/{date}", "2020-01-01")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBestLocation_shouldAcceptTodayDate() throws Exception {
        BestLocationResponse response = new BestLocationResponse("Beach A", new BigDecimal("5.0"), new BigDecimal("5.0"), BigDecimal.ONE);
        when(bestLocationService.findBestLocation(today)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/weather/best-location/{date}", today.format(DateTimeFormatter.ISO_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getBestLocation_shouldReturnBadRequest_whenDateIsTooFarInFuture() throws Exception {
        LocalDate farFutureDate = LocalDate.now().plusDays(17);

        mockMvc.perform(get("/api/weather/best-location/{date}", farFutureDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBestLocation_shouldAcceptMaximumForecastDate() throws Exception {
        LocalDate maxDate = LocalDate.now().plusDays(16);
        BestLocationResponse response = new BestLocationResponse("Beach A", new BigDecimal("5.0"), new BigDecimal("5.0"), BigDecimal.ONE);
        when(bestLocationService.findBestLocation(maxDate)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/weather/best-location/{date}", maxDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
