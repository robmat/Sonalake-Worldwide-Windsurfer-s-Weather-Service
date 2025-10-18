package edu.batodev.windsurf.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherbitData(BigDecimal temp, BigDecimal wind_spd, LocalDate datetime) {
}
