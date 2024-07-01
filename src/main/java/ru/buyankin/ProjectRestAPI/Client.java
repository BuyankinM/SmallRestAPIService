package ru.buyankin.ProjectRestAPI;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import ru.buyankin.ProjectRestAPI.dto.MeasurementDTO;
import ru.buyankin.ProjectRestAPI.dto.SensorDTO;

import java.util.*;

public class Client {
    final static String serverEndpoint = "http://127.0.0.1:8080/sensors/registration";
    final static String measurementEndpoint = "http://127.0.0.1:8080/measurements/add";
    final static int totalMeasurements = 1000;

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        Random rng = new Random();
        String sensorName = "Sensor_" + rng.nextInt(1_000_000);
        SensorDTO sensorDTO = new SensorDTO(sensorName);

        // Add sensor
        HttpEntity<SensorDTO> request = new HttpEntity<>(sensorDTO);
        String response = restTemplate.postForObject(serverEndpoint, request, String.class);
        System.out.println("Sensor added - " + response);

        // Add measurements
        for (int i = 0; i < totalMeasurements; i++) {
            MeasurementDTO measurement = new MeasurementDTO(
                    rng.nextFloat() * 200.0F - 100.0F,
                    rng.nextBoolean(),
                    sensorDTO
            );

            HttpEntity<MeasurementDTO> requestMeasurement = new HttpEntity<>(measurement);
            restTemplate.postForObject(measurementEndpoint, requestMeasurement, String.class);
            if (i % 100 == 0)
                System.out.println("Added measurements - " + i);
        }
    }
}
