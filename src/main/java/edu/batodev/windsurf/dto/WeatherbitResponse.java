package edu.batodev.windsurf.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherbitResponse(List<WeatherbitData> data) {
}
