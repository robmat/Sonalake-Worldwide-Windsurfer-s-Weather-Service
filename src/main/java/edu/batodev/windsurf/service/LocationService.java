package edu.batodev.windsurf.service;

import edu.batodev.windsurf.model.Location;
import edu.batodev.windsurf.repository.LocationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    @PostConstruct
    public void initializeLocations() {
        if (locationRepository.count() == 0) {
            locationRepository.saveAll(List.of(
                new Location(null, "Jastarnia", new BigDecimal("54.70"), new BigDecimal("18.68")),
                new Location(null, "Bridgetown", new BigDecimal("13.10"), new BigDecimal("-59.62")),
                new Location(null, "Fortaleza", new BigDecimal("-3.72"), new BigDecimal("-38.54")),
                new Location(null, "Pissouri", new BigDecimal("34.67"), new BigDecimal("32.70")),
                new Location(null, "Le Morne", new BigDecimal("-20.45"), new BigDecimal("57.31"))
            ));
        }
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }
}
