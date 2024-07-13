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
    private final SensorsService sensorsService;

    @Autowired
    public MeasurementsService(MeasurementsRepository measurementRepository, SensorsService sensorsService) {
        this.measurementRepository = measurementRepository;
        this.sensorsService = sensorsService;
    }

    private static void addRandomDate(Measurement measurement) {
        Random rand = new Random();
        int randomDays = rand.nextInt(300) - 150;
        measurement.setCreatedAt(LocalDateTime.now().plusDays(randomDays));
    }

    public List<Measurement> getAllMeasurements() {
        return measurementRepository.findAll();
    }

    public long countRainyDays() {
        return measurementRepository.countURainyDays();
    }

    @Transactional
    public void addMeasurement(Measurement measurement) {
        addRandomDate(measurement);
        enrichMeasurement(measurement);
    }

    private void enrichMeasurement(Measurement measurement) {
        measurement.setSensor(sensorsService.findByName(measurement.getSensor().getName()).get());
    }
}
