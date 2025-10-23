package edu.batodev.windsurf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Main entry point for the Worldwide Windsurfer's Weather Service application.
 */
@SpringBootApplication
public class Application {

	/**
	 * The main method that starts the Spring Boot application.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/**
	 * Creates a {@link RestTemplate} bean to be used for making HTTP requests.
	 * @return A new {@link RestTemplate} instance.
	 */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
