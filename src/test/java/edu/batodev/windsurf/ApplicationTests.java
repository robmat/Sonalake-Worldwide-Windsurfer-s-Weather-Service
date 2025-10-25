package edu.batodev.windsurf;

import edu.batodev.windsurf.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ApplicationTests {

    @Autowired
    private LocationRepository locationRepository;

    @Test
    void shouldGetAllLocations() {
        var locations = locationRepository.findAll();
        assertThat(locations)
                .isNotEmpty()
                .hasSize(5)
                .extracting("name")
                .containsExactlyInAnyOrder("Jastarnia", "Bridgetown", "Fortaleza", "Pissouri", "Le Morne");
    }
}

