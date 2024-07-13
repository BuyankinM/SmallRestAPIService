package ru.buyankin.ProjectRestAPI.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.buyankin.ProjectRestAPI.dto.SensorDTO;
import ru.buyankin.ProjectRestAPI.models.Sensor;
import ru.buyankin.ProjectRestAPI.services.SensorsService;
import ru.buyankin.ProjectRestAPI.util.ErrorsUtil;
import ru.buyankin.ProjectRestAPI.util.MeasurementErrorResponse;
import ru.buyankin.ProjectRestAPI.util.MeasurementException;
import ru.buyankin.ProjectRestAPI.util.SensorValidator;

@RestController
@RequestMapping("/sensors")
public class SensorController {

    private final ModelMapper modelMapper;
    private final SensorsService sensorsService;
    private final SensorValidator sensorValidator;

    @Autowired
    public SensorController(ModelMapper modelMapper, SensorsService sensorsService, SensorValidator sensorValidator) {
        this.modelMapper = modelMapper;
        this.sensorsService = sensorsService;
        this.sensorValidator = sensorValidator;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registerSensor(@RequestBody @Valid SensorDTO sensorDTO,
                                                     BindingResult bindingResult) {

        Sensor sensorToAdd = convertToSensor(sensorDTO);
        sensorValidator.validate(sensorToAdd, bindingResult);

        if (bindingResult.hasErrors())
            ErrorsUtil.returnErrorsToClient(bindingResult);

        sensorsService.save(sensorToAdd);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementException e) {
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }
}
