package edu.batodev.windsurf.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "weatherbit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherbitConfigProperties {

    @Valid
    private Api api = new Api();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Api {
        @NotBlank
        private String key;
        @NotBlank
        private String url;
    }
}
