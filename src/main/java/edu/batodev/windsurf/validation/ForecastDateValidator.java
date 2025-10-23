package edu.batodev.windsurf.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

/**
 * Validator for the {@link ValidForecastDate} constraint.
 * Checks if a given date is not more than 16 days in the future.
 */
public class ForecastDateValidator implements ConstraintValidator<ValidForecastDate, LocalDate> {
    
    private static final int MAX_FORECAST_DAYS = 16;
    
    /**
     * Checks if the provided date is valid according to the forecast rules.
     * A date is considered valid if it is null, or if it is not after the maximum allowed forecast date (today + 16 days).
     *
     * @param date The date to validate.
     * @param context The context in which the constraint is evaluated.
     * @return {@code true} if the date is valid, {@code false} otherwise.
     */
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) {
            return true; // Let @NotNull handle null values
        }
        LocalDate maxDate = LocalDate.now().plusDays(MAX_FORECAST_DAYS);
        return !date.isAfter(maxDate);
    }
}
