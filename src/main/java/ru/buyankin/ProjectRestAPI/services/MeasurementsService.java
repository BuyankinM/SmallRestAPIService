package ru.buyankin.ProjectRestAPI.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.buyankin.ProjectRestAPI.models.Measurement;
import ru.buyankin.ProjectRestAPI.repositories.MeasurementsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@Transactional(readOnly = true)
public class MeasurementsService {

    private final MeasurementsRepository measurementRepository;

    @Autowired
    public MeasurementsService(MeasurementsRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    @Transactional
    public void save(Measurement measurement) {
        addRandomDate(measurement);
        measurementRepository.save(measurement);
    }

    private static void addRandomDate(Measurement measurement) {
        Random rand = new Random();
        int randomDays = rand.nextInt(300) - 150;
        measurement.setCreatedAt(LocalDateTime.now().plusDays(randomDays));
    }

    public List<Measurement> getAllMeasurements() {
        return measurementRepository.findAll();
    }
}
