package ru.buyankin.ProjectRestAPI.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class MeasurementDTO {
    @NotNull(message = "Temperature could not be empty")
    @Min(value = -100, message = "Temperature year should be greater than -100")
    @Max(value = 100, message = "Temperature year should be lower than 100")
    private Float value;

    @NotNull(message = "Raining property could not be empty")
    private Boolean raining;

    @NotNull(message = "Sensor could not be empty")
    private SensorDTO sensorDTO;

    public MeasurementDTO() {
    }

    public MeasurementDTO(Float value, Boolean raining, SensorDTO sensorDTO) {
        this.value = value;
        this.raining = raining;
        this.sensorDTO = sensorDTO;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public boolean isRaining() {
        return raining;
    }

    public void setRaining(boolean raining) {
        this.raining = raining;
    }

    public SensorDTO getSensorDTO() {
        return sensorDTO;
    }

    public void setSensorDTO(SensorDTO sensorDTO) {
        this.sensorDTO = sensorDTO;
    }
}
