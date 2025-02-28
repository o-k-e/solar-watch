package com.codecool.solarwatch.model.dto.response;

import lombok.Data;

@Data
public class CityResponse {

    private long id;
    private String name;
    private double longitude;
    private double latitude;
    private String country;
}
