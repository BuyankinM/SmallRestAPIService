package ru.buyankin.ProjectRestAPI.util;

public class SensorAlreadyExistsException extends RuntimeException {
    public SensorAlreadyExistsException(String s) {
        super(s);
    }
}
