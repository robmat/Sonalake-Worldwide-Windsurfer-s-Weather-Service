package edu.batodev.windsurf.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(WeatherbitConfigProperties.class)
public class EnableConfiguration {
}
