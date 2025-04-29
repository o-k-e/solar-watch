package com.codecool.solarwatch.model.dto.request;

public record CityModificationRequest(long id, String name, double latitude, double longitude, String country) {
}
