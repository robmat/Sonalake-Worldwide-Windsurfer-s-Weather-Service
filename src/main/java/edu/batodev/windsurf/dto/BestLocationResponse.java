package edu.batodev.windsurf.dto;

import java.math.BigDecimal;

public record BestLocationResponse(String location, BigDecimal temperature, BigDecimal windSpeed, BigDecimal score) {
}
