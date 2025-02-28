package com.codecool.solarwatch.exception;

public class CityNotFoundException extends RuntimeException {
    public CityNotFoundException(String city) {
        super("Provided city name not found: " + city);
    }
}
