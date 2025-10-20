package edu.batodev.windsurf.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class ForecastDateValidator implements ConstraintValidator<ValidForecastDate, LocalDate> {
    
    private static final int MAX_FORECAST_DAYS = 16;
    
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) {
            return true; // Let @NotNull handle null values
        }
        LocalDate maxDate = LocalDate.now().plusDays(MAX_FORECAST_DAYS);
        return !date.isAfter(maxDate);
    }
}
