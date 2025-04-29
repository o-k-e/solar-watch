package com.codecool.solarwatch.model.dto.request;

public record CityCreationRequest(String name, double longitude, double latitude, String country) {
}
