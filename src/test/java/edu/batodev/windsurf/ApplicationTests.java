package edu.batodev.windsurf;

import edu.batodev.windsurf.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplicationTests {

	@Autowired
	private LocationRepository locationRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void shouldGetAllLocations() {
		var locations = locationRepository.findAll();
		assertThat(locations).isNotEmpty();
		assertThat(locations).hasSize(5);
		assertThat(locations)
			.extracting("name")
			.containsExactlyInAnyOrder("Jastarnia", "Bridgetown", "Fortaleza", "Pissouri", "Le Morne");
	}
}

