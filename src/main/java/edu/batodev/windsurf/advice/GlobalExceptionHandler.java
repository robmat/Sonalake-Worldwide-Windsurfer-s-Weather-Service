package edu.batodev.windsurf.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Map<String, String> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getParameterName(), ex.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Map<String, String> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "Request body is missing or unreadable");
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Map<String, String> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getParameter().toString(), ex.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HandlerMethodValidationException.class)
    public Map<String, String> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        Map<String, String> errors = new LinkedHashMap<>();

        ex.getParameterValidationResults().forEach(result ->
                result.getResolvableErrors().forEach(error -> {
                    String field = result.getMethodParameter().getParameterName();
                    String message = error.getDefaultMessage();
                    errors.put(field, message);
                })
        );

        ex.getCrossParameterValidationResults().forEach(result -> {
            String field = Arrays.asList(result.getArguments()).toString();
            String message = result.getDefaultMessage();
            errors.put(field, message);
        });

        return errors;
    }
}
