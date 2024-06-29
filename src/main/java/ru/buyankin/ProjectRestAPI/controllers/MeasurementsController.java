package ru.buyankin.ProjectRestAPI.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.buyankin.ProjectRestAPI.dto.MeasurementDTO;
import ru.buyankin.ProjectRestAPI.dto.SensorDTO;
import ru.buyankin.ProjectRestAPI.models.Measurement;
import ru.buyankin.ProjectRestAPI.models.Sensor;
import ru.buyankin.ProjectRestAPI.services.MeasurementsService;
import ru.buyankin.ProjectRestAPI.services.SensorsService;
import ru.buyankin.ProjectRestAPI.util.MeasurementErrorResponse;
import ru.buyankin.ProjectRestAPI.util.MeasurementNotCreatedException;
import ru.buyankin.ProjectRestAPI.util.SensorValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementsController {

    private final MeasurementsService measurementsService;
    private final SensorsService sensorsService;
    private final SensorValidator sensorValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public MeasurementsController(MeasurementsService measurementsService,
                                  SensorsService sensorsService,
                                  SensorValidator sensorValidator,
                                  ModelMapper modelMapper) {

        this.measurementsService = measurementsService;
        this.sensorsService = sensorsService;
        this.sensorValidator = sensorValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public ResponseEntity<List<MeasurementDTO>> getAllMeasurements() {
        List<MeasurementDTO> measurementDTOs = measurementsService
                .getAllMeasurements()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(measurementDTOs);
    }

    private MeasurementDTO convertToDTO(Measurement measurement) {
        MeasurementDTO measurementDTO = modelMapper.map(measurement, MeasurementDTO.class);
        measurementDTO.setSensorDTO(modelMapper.map(measurement.getSensor(), SensorDTO.class));
        return measurementDTO;
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO,
                                                     BindingResult bindingResult) {

        SensorDTO sensorDTO = measurementDTO.getSensorDTO();
        sensorValidator.validate(sensorDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError error : fieldErrors) {
                errorMsg.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append("; ");
            }
            throw new MeasurementNotCreatedException(errorMsg.toString());
        }

        Sensor sensor = sensorsService.findByName(sensorDTO.getName()).orElse(null);

        Measurement measurement = new Measurement();
        measurement.setSensor(sensor);
        measurement.setValue(measurementDTO.getValue());
        measurement.setRaining(measurementDTO.isRaining());

        measurementsService.save(measurement);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementNotCreatedException e) {
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
