package ru.buyankin.ProjectRestAPI.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.buyankin.ProjectRestAPI.dto.SensorDTO;
import ru.buyankin.ProjectRestAPI.models.Sensor;
import ru.buyankin.ProjectRestAPI.services.SensorsService;

@Component
public class SensorValidator implements Validator {

    private final SensorsService sensorsService;

    @Autowired
    public SensorValidator(SensorsService sensorsService) {
        this.sensorsService = sensorsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Sensor.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SensorDTO sensorDTO = (SensorDTO) target;

        if (sensorDTO.getName() == null || sensorsService.findByName(sensorDTO.getName()).isEmpty())
            errors.rejectValue("sensorDTO", "NotFound", "Sensor not found!");
    }
}
