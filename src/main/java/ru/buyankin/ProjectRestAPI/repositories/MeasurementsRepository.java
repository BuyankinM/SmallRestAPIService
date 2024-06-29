package ru.buyankin.ProjectRestAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.buyankin.ProjectRestAPI.models.Measurement;

@Repository
public interface MeasurementsRepository extends JpaRepository<Measurement, Integer> {
    @Query(value = "SELECT COUNT(DISTINCT created_at::DATE) FROM measurements WHERE raining = true", nativeQuery = true)
    long countURainyDays();
}
