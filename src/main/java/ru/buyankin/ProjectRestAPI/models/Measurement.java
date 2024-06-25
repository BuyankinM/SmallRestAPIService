package ru.buyankin.ProjectRestAPI.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;

@Entity
@Table(name = "measurements")
public class Measurement {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "value")
    @NotEmpty(message = "Temperature could not be empty")
    @Min(value = -100, message = "Temperature year should be greater than -100")
    @Max(value = 100, message = "Temperature year should be lower than 100")
    private float temperature;

    @Column(name = "raining")
    @NotEmpty(message = "Raining property could not be empty")
    private boolean raining;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "sensor_id", referencedColumnName = "id")
    @NotEmpty(message = "Sensor could not be empty")
    private Sensor sensor;

    public Measurement() {
    }

    public Measurement(float temperature, boolean raining, Sensor sensor) {
        this.temperature = temperature;
        this.raining = raining;
        this.sensor = sensor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    @NotEmpty(message = "Raining property could not be empty")
    public boolean isRaining() {
        return raining;
    }

    public void setRaining(boolean raining) {
        this.raining = raining;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
}
