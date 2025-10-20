package edu.batodev.windsurf.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ForecastDateValidator.class)
public @interface ValidForecastDate {
    String message() default "Date must be within the next 16 days";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}