package edu.batodev.windsurf.repository;

import edu.batodev.windsurf.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
