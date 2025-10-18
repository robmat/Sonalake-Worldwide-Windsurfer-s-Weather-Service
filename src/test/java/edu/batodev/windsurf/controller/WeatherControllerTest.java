package edu.batodev.windsurf.controller;

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
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class WeatherControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BestLocationService bestLocationService;

    @InjectMocks
    private WeatherController weatherController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(weatherController).build();
    }

    @Test
    void getBestLocation_shouldReturnBestLocation_whenServiceReturnsData() throws Exception {
        LocalDate date = LocalDate.of(2025, 10, 18);
        BestLocationResponse response = new BestLocationResponse("Beach A", new BigDecimal("5.0"), new BigDecimal("5.0"), BigDecimal.ONE);
        when(bestLocationService.findBestLocation(date)).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/weather/best-location/{date}", date)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value("Beach A"))
                .andExpect(jsonPath("$.score").value(1.0));
    }

    @Test
    void getBestLocation_shouldReturnNotFound_whenServiceReturnsEmpty() throws Exception {
        LocalDate date = LocalDate.of(2025, 10, 18);
        when(bestLocationService.findBestLocation(date)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/weather/best-location/{date}", date)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
