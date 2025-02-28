package com.codecool.solarwatch.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CityCreationRequest {
    private String name;
    private double longitude;
    private double latitude;
    private String country;
}
